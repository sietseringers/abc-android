package net.sietseringers.abc.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import it.unisa.dia.gas.jpbc.Pairing;
import net.sietseringers.abc.*;
import net.sietseringers.abc.android.R;
import net.sietseringers.abc.issuance.CommitmentIssuanceMessage;
import net.sietseringers.abc.issuance.FinishIssuanceMessage;
import net.sietseringers.abc.issuance.RequestIssuanceMessage;
import net.sietseringers.abc.issuance.StartIssuanceMessage;
import org.irmacard.credentials.Attributes;
import org.irmacard.credentials.info.CredentialDescription;
import org.irmacard.credentials.info.DescriptionStore;
import org.irmacard.credentials.info.VerificationDescription;
import org.w3c.dom.Text;

import java.util.Arrays;

public class MainActivity extends Activity {
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		AndroidWalker aw = new AndroidWalker(getResources().getAssets());
		DescriptionStore.setTreeWalker(aw);

		String text = "";
		TextView textView = (TextView) findViewById(R.id.textview);

		try {
			net.sietseringers.androidpbc.AndroidPBC.Initialize();
			textView.setText(test());
		} catch (Exception e) {
			textView.setTextColor(getResources().getColor(R.color.red));
			textView.setText(e.toString());
		}
	}

	public String test() throws Exception {
		StringBuilder sb = new StringBuilder();

		long start;
		long stop;

		start = System.currentTimeMillis();
		Pairing e = SystemParameters.e;
		stop = System.currentTimeMillis();
		sb.append("Instantiated pairing in ").append(stop - start).append(" ms\n");

		// Generate a new private/public keypair
		start = System.currentTimeMillis();
		PrivateKey sk = new PrivateKey(6);
		stop = System.currentTimeMillis();
		sb.append("Created private key in ").append(stop - start).append(" ms\n");

		// Load the "agelower" CredentialDescription from the store
		// Setup the DescriptionStore
		CredentialDescription agelower = DescriptionStore.getInstance().getCredentialDescription((short)10);

		// Build the attributes that we want in our credential
		Attributes attributes = new Attributes();
		for (String name : agelower.getAttributeNames()) {
			attributes.add(name, "yes".getBytes());
		}

		start = System.currentTimeMillis();

		// Build a credential, put it in a card, print its attributes
		CredentialIssuer issuer = new CredentialIssuer(sk);
		CredentialBuilder builder = new CredentialBuilder();

		RequestIssuanceMessage request = builder.generateRequestIssuanceMessage(agelower, attributes);
		StartIssuanceMessage startMessage = issuer.generateStartIssuanceMessage(request);
		CommitmentIssuanceMessage commitMessage = builder.generateCommitmentIssuanceMessage(startMessage);
		FinishIssuanceMessage finishMessage = issuer.generateFinishIssuanceMessage(commitMessage);

		Credential c = builder.generateCredential(finishMessage);

		Credentials card = new Credentials();
		card.set(agelower, c);
		stop = System.currentTimeMillis();
		sb.append("Issuing took ").append(stop - start)
				.append(" ms\n\nAttributes in credential:\n")
				.append(card.getAttributes(agelower).toString()).append("\n\n");

		// Create a disclosure proof
		start = System.currentTimeMillis();
		ProofD proof = c.getDisclosureProof(Util.generateNonce(), Arrays.asList(1, 2, 3));
		stop = System.currentTimeMillis();
		sb.append("Disclosing took ").append(stop - start).append(" ms\n");

		// Verify it directly
		start = System.currentTimeMillis();
		sb.append("Proof validity: ")
				.append(proof.isValid(sk.publicKey)).append("\n");
		stop = System.currentTimeMillis();
		sb.append("Verify 1 took ").append(stop - start).append(" ms\n\n");

		// Verify it and return the contained attributes using a VerificationDescription
		start = System.currentTimeMillis();
		VerificationDescription vd = DescriptionStore.getInstance()
				.getVerificationDescriptionByName("IRMATube", "ageLowerOver18");
		proof = c.getDisclosureProof(vd, Util.generateNonce());
		Attributes disclosed = proof.verify(vd, sk.publicKey);
		sb.append("Disclosed attributes: \n").append(disclosed.toString()).append("\n\n");
		stop = System.currentTimeMillis();
		sb.append("Verify 2 took ").append(stop - start).append(" ms\n");

		return sb.toString();
	}
}
