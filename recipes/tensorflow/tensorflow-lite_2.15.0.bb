inherit cmake

SUMMARY = "Tensorflow Lite"
DESCRIPTION = "TensorFlow Lite C++ Library"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "\
    protobuf \
    protobuf-native \
    jpeg \
    "
SRCREV = "${AUTOREV}"
BRANCH = "iot-ml.lnx.${@'.'.join(d.getVar('PV').split('.')[0:2])}"

SRC_URI = "\
         git://git.codelinaro.org/clo/le/external/github.com/tensorflow/tensorflow.git;protocol=https;branch=${BRANCH};destsuffix=src \
         file://tensorflow-lite.pc.in \
         file://0001-remove-abseil-cpp-build.patch \
         file://0002-Fix-the-compilation-error-of-missing-absl-StrCat-fun.patch \
         "

SRC_URI:remove:qrb5165-rb5 = "\
         file://0001-remove-abseil-cpp-build.patch \
         file://0002-Fix-the-compilation-error-of-missing-absl-StrCat-fun.patch \
"

SRC_URI:remove:kalama = "\
         file://0001-remove-abseil-cpp-build.patch \
         file://0002-Fix-the-compilation-error-of-missing-absl-StrCat-fun.patch \
"

SRC_URI:remove:bengal = "\
         file://0001-remove-abseil-cpp-build.patch \
         file://0002-Fix-the-compilation-error-of-missing-absl-StrCat-fun.patch \
"

SRC_URI:remove:pineapple = "\
         file://0001-remove-abseil-cpp-build.patch \
         file://0002-Fix-the-compilation-error-of-missing-absl-StrCat-fun.patch \
"

# Re-add patchs for qrbx210.since it is removed by SRC_URI:remove:bengal,
# as bengal is included in qrbx210 MACHINEOVERRIDES.
# This ensures the patchs are applied correctly for qrbx210 target.
SRC_URI:append:qrbx210-rbx = "\
         file://0001-remove-abseil-cpp-build.patch;apply=yes \
         file://0002-Fix-the-compilation-error-of-missing-absl-StrCat-fun.patch;apply=yes \
"

S = "${WORKDIR}/src"

OECMAKE_SOURCEPATH = "${S}/tensorflow/lite/c"

DEBUG_PREFIX_MAP:remove = "-fcanon-prefix-map"

do_configure[network] = "1"

MAJOR = "${@d.getVar('PV').split('.')[0]}"

do_configure:prepend() {
    mkdir -p ${WORKDIR}/build
    cd ${WORKDIR}/build
    cmake ../src/tensorflow/lite/c
    find ${WORKDIR}/build -name Makefile -exec rm -r {} \;
    find ${WORKDIR}/build -name cmake_install.cmake -exec rm -r {} \;
    find ${WORKDIR}/build -name CMakeCache.txt -exec rm -r {} \;
    find ${WORKDIR}/build -name CMakeFiles -exec rm -rf {} +
}


OECMAKE_TARGET_COMPILE += "\
    benchmark_model \
    label_image \
    multimodel_label_image \
    inf_diff_run_eval \
    image_classify_run_eval \
    object_detect_run_eval \
    "

EXTRA_OECMAKE += "\
    -DCMAKE_SYSTEM_NAME=Linux \
    -DSYSROOT_INCDIR=${STAGING_INCDIR} \
    -DSYSROOT_LIBDIR=${STAGING_LIBDIR} \
    -DSYSROOT_BINDIR_NATIVE=${STAGING_BINDIR_NATIVE} \
    -DTFLITE_INSTALL_INCDIR=${includedir} \
    -DTFLITE_INSTALL_BINDIR=${bindir} \
    -DTFLITE_INSTALL_LIBDIR=${libdir} \
    -DTFLITE_ENABLE_XNNPACK=ON \
    -DTFLITE_ENABLE_EVALUATION_TOOLS=ON \
    -DTFLITE_ENABLE_NNAPI=OFF \
    -DTFLITE_ENABLE_RUY=ON \
    -DTFLITE_ENABLE_HEXAGON=OFF \
    "

PACKAGECONFIG ?= "gpu"

PACKAGECONFIG[gpu] = " -DTFLITE_ENABLE_GPU=ON ,  -DTFLITE_ENABLE_GPU=OFF, adreno vulkan-headers, adreno"

CC_COMPILER = "${@d.getVar('CC').split(' ')[0].split('/')[-1]}"
LLVM_COMPILER = "${@d.getVar('LLVM_VERSION').split('.')[0]}"
python () {
    if d.getVar('CC_COMPILER') == "clang" and int(d.getVar('LLVM_COMPILER')) <= 10:
        d.appendVar('EXTRA_OECMAKE', ' -DXNNPACK_ENABLE_ARM_BF16=OFF')
}

INSANE_SKIP:${PN} += "dev-so"
FILES:${PN} = "${libdir}/lib*.so* ${bindir}/*"
FILES:${PN}-dev += "${includedir}"

SOLIBS = ".so*"
FILES_SOLIBSDEV = ""

TFLITE_HEADERS="tensorflow/lite tensorflow/core/public tensorflow/core/platform tensorflow/core/lib tensorflow/lite/examples/label_image"

do_install:append() {

    for HPATH in ${TFLITE_HEADERS};
    do
        install -d ${D}${includedir}/${HPATH}
        cd ${S}/${HPATH}
        cp --parents $(find . \( ! -name "*hexagon*" -name "*.h*" \)) ${D}${includedir}/${HPATH}
    done

    install -d ${D}${libdir}
    install ${B}/libtensorflowlite_c.so ${D}${libdir}/libtensorflowlite_c.so.${PV}
    ln -sf libtensorflowlite_c.so.${PV} ${D}${libdir}/libtensorflowlite_c.so.${MAJOR}
    ln -sf libtensorflowlite_c.so.${MAJOR} ${D}${libdir}/libtensorflowlite_c.so

    install -d ${D}${includedir}/third_party/eigen3/Eigen
    install -m 0555 ${S}/third_party/eigen3/Eigen/* ${D}${includedir}/third_party/eigen3/Eigen/

    install -d ${D}${includedir}/Eigen
    cp -r ${B}/eigen/Eigen ${D}${includedir}/

    install -d ${D}${includedir}/third_party/eigen3/unsupported/Eigen
    cp -r ${S}/third_party/eigen3/unsupported/Eigen/* ${D}${includedir}/third_party/eigen3/unsupported/Eigen/

    cp -r ${B}/eigen/unsupported ${D}${includedir}/

    install -d ${D}${includedir}/gemmlowp

    cd ${B}/gemmlowp
    cp --parents $(find . -name "*.h*") ${D}${includedir}/gemmlowp/

    install -d ${D}${includedir}/ruy

    cd ${B}/ruy/ruy
    cp --parents $(find . -name "*.h*") ${D}${includedir}/ruy/

    install -d ${D}${includedir}/flatbuffers

    cd ${B}/flatbuffers/include
    cp  --parents $(find . -name "*.h*") ${D}${includedir}/

    install -d ${D}${libdir}/pkgconfig
    install -m 0644 ${WORKDIR}/tensorflow-lite.pc.in ${D}${libdir}/pkgconfig/tensorflow-lite.pc
    sed -i 's:@version@:${PV}:g
        s:@libdir@:${libdir}:g
        s:@includedir@:${includedir}:g' ${D}${libdir}/pkgconfig/tensorflow-lite.pc

}
