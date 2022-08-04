inherit cmake sdllvm

SUMMARY = "Android NN framework"
DESCRIPTION = "\
 Android NN framework"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

# The revision of the recipe used to build the package.
PV = "1.0"
PR = "r0"

# Dependencies.
DEPENDS += "libbase"
DEPENDS += "liblog"
DEPENDS += "libutils"
DEPENDS += "tensorflow-lite"
DEPENDS += "gtest"
DEPENDS += "libfmq"
DEPENDS += "openssl"

SRCREV = "a457c5ee39ef87274b363d14a402d1e202dd401f"
SRC_URI = "git://git.codelinaro.org/clo/la/platform/frameworks/ml/;protocol=https;branch=iot-ml.lnx.2.0.r6-rel; \
        file://0001-nn-framework-Add-cmake-option-to-enable-disable-CPU-.patch \
        "
S = "${WORKDIR}/git"

# Temporary data path
TEMP_DIR = "/data/local/tmp"

#Set Package Config value vts for enabling vts compilation
#PACKAGECONFIG ??= " vts"

EXTRA_OECMAKE += " -DSYSROOT_INCDIR=${STAGING_INCDIR}"
EXTRA_OECMAKE += " -DSYSROOT_LIBDIR=${STAGING_LIBDIR}"
EXTRA_OECMAKE += " -DNNAPI_LIB_DIR=${libdir}"

PACKAGECONFIG[vts] = "-DVTS_TEMP_PATH=${TEMP_DIR} -DUSE_VTS=ON,,,"

SOLIBS = ".so*"
FILES_SOLIBSDEV = ""

FILES_${PN}-libsample-minimal-nn-hal-dbg    = "${libdir}/nn/.debug/libsample-minimal-nn-hal.*"
FILES_${PN}-libsample-minimal-nn-hal        = "${libdir}/nn/libsample-minimal-nn-hal.so"
FILES_${PN}-libsample-minimal-nn-hal-dev    = "${libdir}/nn/libsample-minimal-nn-hal.so ${includedir}"

FILES_${PN} = "${libdir}/lib*.so ${bindir}"
FILES_${PN} += "${libdir}/nn/lib*.so"

FILES_${PN}-dev = "${libdir}/lib*.so ${includedir} ${bindir}"
FILES_${PN}-dev += "${libdir}/nn/lib*.so"

FILES_${PN}-dbg = "${libdir}/.debug"
FILES_${PN}-dbg += "${libdir}/nn/.debug"

FILES_${PN} += "${TEMP_DIR}"

PACKAGES = "${PN} ${PN}-dbg ${PN}-dev"
