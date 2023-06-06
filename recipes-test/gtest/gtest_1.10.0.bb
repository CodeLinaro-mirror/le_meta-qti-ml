DESCRIPTION = "Google's framework for writing C++ tests"
HOMEPAGE = "https://github.com/google/googletest"
SECTION = "libs"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://googlemock/LICENSE;md5=cbbd27594afd089daa160d3a16dd515a \
                    file://googletest/LICENSE;md5=cbbd27594afd089daa160d3a16dd515a"

PROVIDES += "gmock"

S = "${WORKDIR}/git"
SRCREV = "${AUTOREV}"
BRANCH = "googletest/v1.10.x"
SRC_URI = "\
    git://git.codelinaro.org/clo/qsdk/googletest.git;protocol=https;branch=${BRANCH} \
    file://0001-Add-pkg-config-support.patch \
"
SRC_URI[sha256sum] = "8ee3fb18a5f547e35f76fe3ce773f0a0c789f34291e310cd8217deb9e7478c54"

inherit cmake

EXTRA_OECMAKE = "-DBUILD_SHARED_LIBS=ON"

BBCLASSEXTEND = "native nativesdk"

FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/*.so"
