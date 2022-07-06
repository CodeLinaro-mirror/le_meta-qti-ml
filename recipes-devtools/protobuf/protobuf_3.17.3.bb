SUMMARY = "Protocol Buffers - structured data serialisation mechanism"
DESCRIPTION = "Protocol Buffers are a way of encoding structured data in an \
efficient yet extensible format. Google uses Protocol Buffers for almost \
all of its internal RPC protocols and file formats."
HOMEPAGE = "https://github.com/google/protobuf"
SECTION = "console/tools"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=37b5762e07f0af8c74ce80a8bda4266b"

DEPENDS = "zlib"
DEPENDS_append_class-target = " protobuf-native"

SRCREV = "909a0f36a10075c4b4bc70fdee2c7e32dd612a72"

SRC_URI = "git://github.com/protocolbuffers/protobuf.git"

S = "${WORKDIR}/git"

inherit autotools-brokensep pkgconfig sdllvm

PACKAGECONFIG ??= ""
PACKAGECONFIG[python] = ",,"

EXTRA_OECONF += "--with-protoc=echo --with-pic"

LANG_SUPPORT = "cpp ${@bb.utils.contains('PACKAGECONFIG', 'python', 'python', '', d)}"

do_configure_prepend() {
	sed -i 's/AC_CONFIG_SUBDIRS(\[third_party\/googletest\])//g' ${S}/configure.ac
}

PACKAGE_BEFORE_PN = "${PN}-compiler ${PN}-lite"

FILES_${PN}-compiler = "${bindir} ${libdir}/libprotoc${SOLIBS}"
FILES_${PN}-lite = "${libdir}/libprotobuf-lite${SOLIBS}"

RDEPENDS_${PN}-compiler = "${PN}"
RDEPENDS_${PN}-dev += "${PN}-compiler"

BBCLASSEXTEND = "native nativesdk"
