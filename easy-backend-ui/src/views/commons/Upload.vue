<template>
    <div class="component-upload-image">
        <el-upload
                drag
                :action="uploadImgUrl"
                :on-success="handleUploadSuccess"
                :before-upload="handleBeforeUpload"
                :on-error="handleUploadError"
                name="file"
                :show-file-list="false"
                :headers="headers"
        >
             <i class="el-icon-upload"></i>
             <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        </el-upload>
        <el-row>
            <span>
                {{uploadUrlPath}}
            </span>

        </el-row>
    </div>
</template>

<script>
    import { getToken } from '@/utils/auth';

    export default {
        components: {},
        data() {
            return {
                uploadImgUrl:  "http://127.0.0.1:9527/v1/admin/common/upload", // 上传的图片服务器地址
                headers: {
                    Authorization: "Bearer " + getToken(),
                },
                uploadUrlPath: "没有文件上传",
            };
        },
        props: {
            value: {
                type: String,
                default: "",
            },
        },
        methods: {
            handleUploadSuccess(res) {
                this.uploadUrlPath = JSON.stringify(res);
                this.loading.close();
            },
            handleBeforeUpload() {
                this.loading = this.$loading({
                    lock: true,
                    text: "上传中",
                    background: "rgba(0, 0, 0, 0.7)",
                });
            },
            handleUploadError() {
                this.$message({
                    type: "error",
                    message: "上传失败",
                });
                this.loading.close();
            },
        },
        watch: {},
    };
</script>

<style scoped lang="scss">
    .avatar {
        width: 100%;
        height: 100%;
    }
</style>