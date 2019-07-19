inherit cmake

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
DEPENDS = "libbase"
DEPENDS += "liblog"
DEPENDS += "libutils"
DEPENDS += "tensorflow-lite"
DEPENDS += "gtest"

FILESPATH =+ "${WORKSPACE}/frameworks/ml/:"
SRC_URI = "file://nn"
S = "${WORKDIR}/nn"

EXTRA_OECMAKE += " -DSYSROOT_INCDIR=${STAGING_INCDIR}"
EXTRA_OECMAKE += " -DSYSROOT_LIBDIR=${STAGING_LIBDIR}"

SOLIBS = ".so*"
FILES_SOLIBSDEV = ""

FILES_${PN}-libsample-minimal-nn-hal-dbg    = "${libdir}/nn/.debug/libsample-minimal-nn-hal.*"
FILES_${PN}-libsample-minimal-nn-hal        = "${libdir}/nn/libsample-minimal-nn-hal.so"
FILES_${PN}-libsample-minimal-nn-hal-dev    = "${libdir}/nn/libsample-minimal-nn-hal.so ${includedir}"

FILES_${PN} = "${libdir}/lib*.so"
FILES_${PN} += "${libdir}/nn/lib*.so"

FILES_${PN}-dev = "${libdir}/lib*.so ${includedir}"
FILES_${PN}-dev += "${libdir}/nn/lib*.so"

FILES_${PN}-dbg = "${libdir}/.debug"
FILES_${PN}-dbg += "${libdir}/nn/.debug"

PACKAGES = "${PN} ${PN}-dbg ${PN}-dev"
