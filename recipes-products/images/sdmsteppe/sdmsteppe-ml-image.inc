# ML Open source Packages
# Following packages support only 32-bit compilation.
# When multilib is enabled, compile them with lib32- prefix.

IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES','tflite-enable', 'tensorflow-lite', '', d)}"
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES','tflite-enable', 'nn-framework', '', d)}"
