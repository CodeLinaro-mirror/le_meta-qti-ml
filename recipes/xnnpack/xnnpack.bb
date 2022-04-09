inherit cmake sdllvm

SUMMARY = "XNNPACK is a based on QNNPACK library"
DESCRIPTION = "XNNPACK is a highly optimized library of floating-point neural network inference \
   operators for ARM, WebAssembly, and x86 platforms. XNNPACK is not intended \
   for direct use by deep learning practitioners and researchers; \
   instead it provides low-level performance primitives for accelerating high-level machine learning frameworks, such as \
   TensorFlow Lite, TensorFlow.js, PyTorch, and MediaPipe."

HOMEPAGE = "https://source.codeaurora.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=afa8f8a91390ab659c837da57124977c"

SRCREV = "05702cf4099ad019ad1abb8ba656bfe04304f32a"
SRC_URI = "git://git.codelinaro.org/clo/le/XNNPACK.git;protocol=https;branch=XNNPACK/master"
SRC_URI += "file://0001-xnnpack-change-MAKE_SYSTEM_PROCESSOR.patch"

S = "${WORKDIR}/git"

OECMAKE_SOURCEPATH = "${S}"

SOLIBS = ".so*"
FILES_SOLIBSDEV = ""

