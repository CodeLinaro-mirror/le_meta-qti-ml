inherit cmake

SUMMARY = "ARM NN Driver"
DESCRIPTION = "ARM NN Driver"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=0835ade698e0bcf8506ecda2f7b4f302"

# The revision of the recipe used to build the package.
PR = "r0"

# Dependencies.
DEPENDS = "libbase"
DEPENDS += "liblog"
DEPENDS += "libutils"
DEPENDS += "nn-framework"
DEPENDS += "python-scons-native"
DEPENDS += "curl-native"
DEPENDS += "boost"

do_patch[depends] = "curl-native:do_populate_sysroot python-scons-native:do_populate_sysroot"

SRC_URI = "git://github.com/ARM-software/android-nn-driver.git;branch=branches/android-nn-driver_20_02;rev=v20.02"
SRC_URI += "file://0001-android-nn-driver-Port-to-LE.patch"
SRC_URI += "file://0002-android-nn-driver-lower-the-priority-for-ARM-NN-HAL.patch"

S = "${WORKDIR}/git"

EXTRA_OECMAKE += " -DSYSROOT_INCDIR=${STAGING_INCDIR}"
EXTRA_OECMAKE += " -DSYSROOT_LIBDIR=${STAGING_LIBDIR}"
EXTRA_OECMAKE += " -DINSTALL_LIB_DIR=${libdir}"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

setup_script() {
    pushd ${S}
    ./setup.sh
    cd armnn
    ARMNN_PATCH=`echo ${BB_FILENAME} | sed 's/.*://' | sed 's%/[^/]*$%/%'`
    ARMNN_PATCH=${ARMNN_PATCH}/files/0001-armnn-Modify-CMake-files-to-work-on-LE.patch
    git apply ${ARMNN_PATCH}
    cd ../clframework
    CLF_PATCH=`echo ${BB_FILENAME} | sed 's/.*://' | sed 's%/[^/]*$%/%'`
    CLF_PATCH=${CLF_PATCH}/files/0001-clframework-fix-macro-redefinition-issue.patch
    git apply ${CLF_PATCH}
    popd
}

python do_patch() {
    bb.build.exec_func('patch_do_patch', d)
    bb.build.exec_func("setup_script", d)
}

PACKAGES = "${PN} ${PN}-dbg ${PN}-dev"

FILES_${PN}-libarmnn-driver-dbg    = "${libdir}/nn/.debug/libarmnn-driver-1.2.*"
FILES_${PN}-libarmnn-driver        = "${libdir}/nn/libarmnn-driver-1.2.so"
FILES_${PN}-libarmnn-driver-dev    = "${libdir}/nn/libarmnn-driver-1.2.so ${includedir}"

FILES_${PN} = "${libdir}/lib*.so*"
FILES_${PN} += "${libdir}/nn/lib*.so"

FILES_${PN}-dev = "${libdir}/lib*.so ${includedir}"
FILES_${PN}-dev += "${libdir}/nn/lib*.so"

FILES_${PN}-dbg = "${libdir}/.debug"
FILES_${PN}-dbg += "${libdir}/nn/.debug"

do_package_qa[noexec] = "1"
