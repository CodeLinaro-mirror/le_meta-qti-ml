inherit cmake

SUMMARY = "nsync"
DESCRIPTION = "nsync is a C library that exports various synchronization primitives, such as mutexes"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

SRCREV = "1.24.0"
BRANCH = "master"

SRC_URI = "git://github.com/google/nsync.git;protocol=https;branch=${BRANCH}"

S  = "${WORKDIR}/git"

EXTRA_OECMAKE = "\
	-DBUILD_SHARED_LIBS=ON \
	-DNSYNC_ENABLE_TESTS=OFF \
	"
