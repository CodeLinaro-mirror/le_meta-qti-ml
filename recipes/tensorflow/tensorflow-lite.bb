inherit cmake

SUMMARY = "Tensorflow Lite"
DESCRIPTION = "TensorFlow Lite C++ Library"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

# The revision of the recipe used to build the package.
PV = "1.14"
PR = "r0"

# Dependencies.
DEPENDS = "unzip-native"
DEPENDS += "curl-native"
DEPENDS += "zlib"

do_patch[depends] = "curl-native:do_populate_sysroot unzip-native:do_populate_sysroot"

FILESPATH =+ "${WORKSPACE}/external/:"
SRC_URI = "file://tensorflow"
S = "${WORKDIR}/tensorflow"

# Installation directories.
INSTALL_INCDIR := "${includedir}"
INSTALL_BINDIR := "${bindir}"
INSTALL_LIBDIR := "${libdir}"

EXTRA_OECMAKE += " -DSYSROOT_INCDIR=${STAGING_INCDIR}"
EXTRA_OECMAKE += " -DSYSROOT_LIBDIR=${STAGING_LIBDIR}"
EXTRA_OECMAKE += "-DTFLITE_INSTALL_INCDIR=${INSTALL_INCDIR}"
EXTRA_OECMAKE += "-DTFLITE_INSTALL_BINDIR=${INSTALL_BINDIR}"
EXTRA_OECMAKE += "-DTFLITE_INSTALL_LIBDIR=${INSTALL_LIBDIR}"

FILES_${PN}      += "${INSTALL_BINDIR}"
FILES_${PN}      += "${INSTALL_LIBDIR}"
FILES_${PN}-dev  += "${INSTALL_INCDIR}"

SOLIBS = ".so*"
FILES_SOLIBSDEV = ""

download_dependencies() {
    ${S}/tensorflow/lite/tools/make/download_dependencies.sh
}

python do_patch() {
    bb.build.exec_func('patch_do_patch', d)
    bb.build.exec_func("download_dependencies", d)
}

do_install_append() {
    install -d ${D}${includedir}/third_party/eigen3/Eigen
    install -m 0555 ${S}/third_party/eigen3/Eigen/* ${D}${includedir}/third_party/eigen3/Eigen/

    install -d ${D}${includedir}/Eigen
    cp -r ${S}/tensorflow/lite/tools/make/downloads/eigen/Eigen ${D}${includedir}/

    install -d ${D}${includedir}/third_party/eigen3/unsupported/Eigen/CXX11
    cp -r ${S}/third_party/eigen3/unsupported/Eigen/CXX11/* ${D}${includedir}/third_party/eigen3/unsupported/Eigen/CXX11/

    cp -r ${S}/tensorflow/lite/tools/make/downloads/eigen/unsupported ${D}${includedir}/

    install -d ${D}${includedir}/absl

    cd ${S}/tensorflow/lite/tools/make/downloads/absl
    cp --parents $(find . -name "*.h*") ${D}${includedir}/

    install -d ${D}${includedir}/gemmlowp

    cd ${S}/tensorflow/lite/tools/make/downloads/gemmlowp
    cp --parents $(find . -name "*.h*") ${D}${includedir}/gemmlowp/

    install -d ${D}${includedir}/flatbuffers
    install -m 0555 ${S}/tensorflow/lite/tools/make/downloads/flatbuffers/include/flatbuffers/* ${D}${includedir}/flatbuffers/
}
