/*
 * MainActivity.java
 * Copyright (C) 2015 Sietse Ringers
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */

package net.sietseringers.abc.android;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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
import org.irmacard.credentials.info.InfoException;
import org.irmacard.credentials.info.VerificationDescription;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Make scrollable
		TextView textView = (TextView) findViewById(R.id.textview);
		textView.setMovementMethod(new ScrollingMovementMethod());

		// Streams for capturing the test output
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(byteStream, true);

		AndroidWalker aw = new AndroidWalker(getResources().getAssets());
		DescriptionStore.setTreeWalker(aw);

		try {
			MainTest.setDescriptionStore(DescriptionStore.getInstance());
			MainTest.setPrintStream(out);

			JUnitCore runner = new JUnitCore();
			runner.addListener(new AbcTestListener(out));
			Result r = runner.run(MainTest.class);
		} catch (InfoException e) {
			e.printStackTrace(out);
		}

		String text = new String(byteStream.toByteArray());
		textView.setText(text);
	}
}
