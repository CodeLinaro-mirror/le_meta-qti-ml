SUMMARY = "QTI Machine Learning package groups"
LICENSE = "BSD-3-Clause"

inherit packagegroup

PROVIDES = "${PACKAGES}"

PACKAGES = ' \
    packagegroup-qti-ml \
    \
    ${@bb.utils.contains("DISTRO_FEATURES", "tensorflow-lite", "packagegroup-qti-ml-tflite", "", d)} \
    '

RDEPENDS_packagegroup-qti-ml = ' \
    ${@bb.utils.contains("DISTRO_FEATURES", "tensorflow-lite", "packagegroup-qti-ml-tflite", "", d)} \
    libgomp \
    '

RDEPENDS_packagegroup-qti-ml-tflite = " \
    tensorflow-lite \
    ${@bb.utils.contains("MACHINE_FEATURES", "qti-tflite-delegate", "nn-framework", "", d)} \
    "
