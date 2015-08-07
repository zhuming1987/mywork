LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under, src)
LOCAL_STATIC_JAVA_LIBRARIES := TvSDK

LOCAL_CERTIFICATE := platform
LOCAL_PROGUARD_ENABLED := disabled
LOCAL_DEX_PREOPT := false

LOCAL_PACKAGE_NAME := TvApplication
LOCAL_MODULE_PATH := $(PRODUCT_OUT)/system/vendor/app

include $(BUILD_PACKAGE)
