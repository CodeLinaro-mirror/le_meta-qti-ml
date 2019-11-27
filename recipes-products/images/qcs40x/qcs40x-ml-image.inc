# ml Open source Packages
# Following packages support 32-bit and 64-bit compilation.
# When multilib is enabled, compile them with lib32- prefix.

IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES','tensorflow-lite', 'tensorflow-lite', '', d)}"
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES','tflite-hw-delegate', 'nn-framework', '', d)}"
