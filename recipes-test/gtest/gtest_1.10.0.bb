DESCRIPTION = "Google's framework for writing C++ tests"
HOMEPAGE = "https://github.com/google/googletest"
SECTION = "libs"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://googlemock/LICENSE;md5=cbbd27594afd089daa160d3a16dd515a \
                    file://googletest/LICENSE;md5=cbbd27594afd089daa160d3a16dd515a"

PROVIDES += "gmock"

S = "${WORKDIR}/git"
SRCREV = "release-1.10.0"
SRC_URI = "\
    git://github.com/google/googletest.git;protocol=git; \
    file://0001-Add-pkg-config-support.patch \
"

inherit cmake

EXTRA_OECMAKE = "-DBUILD_SHARED_LIBS=ON"

BBCLASSEXTEND = "native nativesdk"

FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/*.so"
