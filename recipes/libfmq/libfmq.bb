inherit cmake

SUMMARY = "Fast Message Queue (FMQ)"
DESCRIPTION = "\
 FMQ creates message queues with the desired properties"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "1.0"
PR = "r0"

# Dependencies.
DEPENDS = "libbase"
DEPENDS += "liblog"
DEPENDS += "libutils"
DEPENDS += "libcutils"

SRCREV = "2172954aa2b2afd2de4f44b8fd25a1a1ac5f2d3e"

SRC_URI = "git://git.codelinaro.org/clo/la/platform/system/libfmq.git;branch=caf_migration/ks-aosp.lnx.2.0.r12-rel"
SRC_URI += "file://0001-libfmq-Porting-to-Linux-Embedded.patch"

S = "${WORKDIR}/git"

EXTRA_OECMAKE += " -DSYSROOT_INCDIR=${STAGING_INCDIR}"
EXTRA_OECMAKE += " -DSYSROOT_LIBDIR=${STAGING_LIBDIR}"
EXTRA_OECMAKE += " -DLIBFMQ_INSTALL_LIBDIR=${libdir}"

SOLIBS = ".so*"
FILES_SOLIBSDEV = ""
