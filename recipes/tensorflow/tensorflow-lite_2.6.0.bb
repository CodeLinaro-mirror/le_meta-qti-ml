inherit cmake sdllvm

SUMMARY = "Tensorflow Lite"
DESCRIPTION = "TensorFlow Lite C++ Library"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "\
	unzip-native \
	curl-native \
	zlib \
	protobuf \
	protobuf-native \
	jpeg \
	${@bb.utils.contains('COMBINED_FEATURES', 'qti-cdsp', 'adsprpc', '', d)} \
	${@bb.utils.contains('COMBINED_FEATURES', 'opencl', 'adreno200', '', d)} \
	flatbuffers \
	flatbuffers-native \
	nsync \
	vulkan-headers \
	"

PACKAGECONFIG ??= " \
	${@bb.utils.contains('COMBINED_FEATURES', 'qti-cdsp', 'qti-dsp', '', d)} \
	${@bb.utils.contains('COMBINED_FEATURES', 'opencl', 'qti-gpu', '', d)} \
	"

SRCREV = "${AUTOREV}"
BRANCH = "github.com/r${@'.'.join(d.getVar('PV').split('.')[0:2])}"

SRC_URI = "\
	${CLO_LE_GIT}/external/github.com/tensorflow/tensorflow.git;protocol=https;branch=${BRANCH} \
	file://0001-tensorflow-lite-Bring-up-TFLite-on-LE-platforms.patch \
	file://0002-tensorflow-lite-Enhance-TFLite-LE-Build.patch \
	file://0003-tensorflow-lite-Enable-AveragePool2D-nnapi-delegatio.patch \
	file://0004-tensorflow-lite-Enable-align-corners-in-Bilinear-res.patch \
	file://0005-tensorflow-lite-Enable-building-TFLite-as-a-shared-l.patch \
	file://0006-tensorflow-lite-Add-clang-support.patch \
	file://0007-tensorflow-lite-Integrate-TFLite-Accuracy-Tools.patch \
	file://0008-tensorflow-lite-Enable-tflite-profiling.patch \
	file://0009-tensorflow-lite-Add-support-for-LeakyReLU-in-Hexagon.patch \
	file://0010-tensorflow-lite-Use-dynamic-loading-for-cdsprpc.patch \
	file://0011-tensorflow-lite-Improve-accuracy-for-depthwise_conv2.patch \
	file://0012-tensorflow-lite-Use-XNNPACK-repository-from-code-lin.patch \
	file://tensorflow-lite.pc.in \
	"

S = "${WORKDIR}/git"

OECMAKE_SOURCEPATH = "${S}/tensorflow/lite"
do_configure:prepend() {
    mkdir -p ${WORKDIR}/build
    cd ${WORKDIR}/build
    cmake ../git/tensorflow/lite/
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
	-DTFLITE_ENABLE_EVALUATION_TOOLS=ON \
	"
PACKAGECONFIG[qti-dsp] = " -DTFLITE_ENABLE_HEXAGON=true ,,,"
PACKAGECONFIG[qti-gpu] = " -DTFLITE_ENABLE_GPU=true ,,,"

FILES_${PN} = "${libdir}/lib*.so ${bindir}/*"
FILES_${PN}-dev += "${includedir}"

SOLIBS = ".so*"
FILES_SOLIBSDEV = ""

do_install:append() {

	local TFLITE_HEADERS=(\
	"tensorflow/lite" \
	"tensorflow/core/public" \
	"tensorflow/core/platform" \
	"tensorflow/core/lib" \
	"tensorflow/lite/examples/label_image" \
	)

	for HPATH in ${TFLITE_HEADERS[@]};
	do
		install -d ${D}${includedir}/$HPATH
		cd ${S}/$HPATH
		cp --parents $(find . -name "*.h*") ${D}${includedir}/$HPATH
	done

	install -d ${D}${libdir}
	install ${B}/libtensorflow*.so ${D}${libdir}/

	install -d ${D}${includedir}/third_party/eigen3/Eigen
	install -m 0555 ${S}/third_party/eigen3/Eigen/* ${D}${includedir}/third_party/eigen3/Eigen/

	install -d ${D}${includedir}/Eigen
	cp -r ${B}/eigen/Eigen ${D}${includedir}/

	install -d ${D}${includedir}/third_party/eigen3/unsupported/Eigen
	cp -r ${S}/third_party/eigen3/unsupported/Eigen/* ${D}${includedir}/third_party/eigen3/unsupported/Eigen/

	cp -r ${B}/eigen/unsupported ${D}${includedir}/

	install -d ${D}${includedir}/absl

	cd ${B}/abseil-cpp/absl
	cp --parents $(find . -name "*.h*") ${D}${includedir}/absl/
	install -m 0644 numeric/int128_have_intrinsic.inc ${D}${includedir}/absl/numeric/

	install -d ${D}${includedir}/gemmlowp

	cd ${B}/gemmlowp
	cp --parents $(find . -name "*.h*") ${D}${includedir}/gemmlowp/

	install -d ${D}${includedir}/ruy

	cd ${B}/ruy/ruy
	cp --parents $(find . -name "*.h*") ${D}${includedir}/ruy/

	install -d ${D}${libdir}/pkgconfig
	install -m 0644 ${WORKDIR}/tensorflow-lite.pc.in ${D}${libdir}/pkgconfig/tensorflow-lite.pc
	sed -i 's:@version@:${PV}:g
		s:@libdir@:${libdir}:g
		s:@includedir@:${includedir}:g' ${D}${libdir}/pkgconfig/tensorflow-lite.pc

}
