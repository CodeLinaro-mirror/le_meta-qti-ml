# ML Open source Packages
# Following packages support only 32-bit compilation.
# When multilib is enabled, compile them with lib32- prefix.

IMAGE_INSTALL += "${@bb.utils.contains('MULTILIB_VARIANTS', 'lib32', 'lib32-tensorflow-lite', 'tensorflow-lite', d)}"
IMAGE_INSTALL += "${@bb.utils.contains('MULTILIB_VARIANTS', 'lib32', 'lib32-nn-framework', 'nn-framework', d)}"