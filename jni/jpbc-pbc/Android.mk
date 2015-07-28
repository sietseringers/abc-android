LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := jbpc-pbc

LOCAL_C_INCLUDES := $(LOCAL_PATH)/include

LOCAL_SRC_FILES = arith/field.c arith/z.c \
	arith/naivefp.c arith/fastfp.c \
	arith/fp.c arith/fasterfp.c arith/montfp.c \
	arith/ternary_extension_field.c \
	arith/multiz.c \
	arith/dlog.c \
	arith/fieldquadratic.c arith/poly.c \
	arith/random.c arith/init_random.c \
	misc/darray.c misc/symtab.c misc/get_time.c \
	misc/utils.c misc/memory.c misc/extend_printf.c \
	ecc/mpc.c ecc/mnt.c ecc/hilbert.c ecc/curve.c ecc/pairing.c \
	ecc/singular.c \
	ecc/eta_T_3.c \
	ecc/param.c ecc/a_param.c ecc/d_param.c ecc/e_param.c \
	ecc/f_param.c ecc/g_param.c \
	pbc/parser.tab.c pbc/lex.yy.c pbc/pbc.c pbc/pbc_getline.c misc/darray.c misc/symtab.c \



# libpbc_la_LDFLAGS = -lgmp -lm -version-info $(SO_VERSION) $(PBC_LDFLAGS) $(LIBPBC_LDFLAGS)

# LDADD = libpbc.la -lgmp -lm

include $(BUILD_SHARED_LIBRARY)
