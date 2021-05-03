<template>
    <a-layout>
        <a-layout-content
                :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
            <a-upload
                    class="inline-block"
                    v-model:file-list="fileList"
                    name="file"
                    action="http://127.0.0.1:8880/import"
                    :headers="headers"
                    @change="handleChange"
            >
                <a-button>
                    <upload-outlined />
                    快速导入
                </a-button>

            </a-upload>
            <a-button  class="editable-add-btn" type="primary" @click="showDrawer" style="margin: 8px">
                <PlusOutlined />
                添加功能
            </a-button>

            <a-drawer
                    title="添加功能"
                    :width="720"
                    :visible="visible"
                    :body-style="{ paddingBottom: '80px' }"
                    @close="onClose"
            >
                <a-form :model="form" :rules="rules" layout="vertical">
                    <a-row :gutter="16">
                        <a-col :span="12">
                            <a-form-item label="功能名称" name="name">
                                <a-input v-model:value="form.name" placeholder="请输入您的功能名称" />
                            </a-form-item>
                        </a-col>
                        <a-col :span="12">
                            <a-form-item label="标识符" name="id">
                                <a-input v-model:value="form.id" placeholder="请输入您的标识符" />
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="16">

                        <a-col :span="12">
                            <a-form-item label="数据类型" name="type">
                                <a-select placeholder="请选择您的数据类型" v-model:value="form.type">
                                    <a-select-option value="float">float</a-select-option>
                                    <a-select-option value="double">double</a-select-option>
                                    <a-select-option value="bool">bool</a-select-option>
                                    <a-select-option value="text">text</a-select-option>
                                    <a-select-option value="date">date</a-select-option>
                                    <a-select-option value="int">int32</a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                    </a-row>
                    <a-row :gutter="16">
                        <a-col :span="24">
                            <a-form-item label="Description" name="description">
                                <a-textarea
                                        v-model:value="form.description"
                                        :rows="4"
                                        placeholder="请输入描述"
                                />
                            </a-form-item>
                        </a-col>
                    </a-row>
                </a-form>
                <div
                                    :style="{
                    position: 'absolute',
                    right: 0,
                    bottom: 0,
                    width: '100%',
                    borderTop: '1px solid #e9e9e9',
                    padding: '10px 16px',
                    background: '#fff',
                    textAlign: 'right',
                    zIndex: 1,
                  }"
                >
                    <a-button style="margin-right: 8px" @click="onClose">Cancel</a-button>
                    <a-button type="primary" @click="handleAdd(form.id)" >Submit</a-button>
                </div>
            </a-drawer>
            <a-divider />

            <a-table :columns="columns" :data-source="dataSource" bordered>
                <template v-for="col in ['name', 'id', 'type']" #[col]="{ text }" :key="col">
                    <div>
                        {{ text }}
                    </div>
                </template>
                <template #operation="{ record }">
                    <a-tag @click="showDrawer(record.key)">Edit</a-tag>
                    <a-tag @click="onDelete(record.key)">Delete</a-tag>
                </template>
            </a-table>


        </a-layout-content>
    </a-layout>
</template>
<script lang="ts">
    import { message } from 'ant-design-vue';
    import { defineComponent, reactive, ref, Ref, UnwrapRef } from 'vue';

    const columns = [
        {
            title: '功能名称',
            dataIndex: 'name',
            width: '25%',
            slots: { customRender: 'name' },
        },
        {
            title: '标识符',
            dataIndex: 'id',
            width: '15%',
            slots: { customRender: 'id' },
        },
        {
            title: '数据类型',
            dataIndex: 'type',
            width: '40%',
            slots: { customRender: 'type' },
        },
        {
            title: '操作',
            dataIndex: 'operation',
            slots: { customRender: 'operation' },
        },
    ];
    interface DataItem {
        key: number;
        name: string;
        id: string;
        type: string;
    }
    const data: DataItem[] = [];

    interface FileItem {
        uid: string;
        name?: string;
        status?: string;
        response?: string;
        url?: string;
    }

    interface FileInfo {
        file: FileItem;
        fileList: FileItem[];
    }

    export default defineComponent({
        setup() {
            const form = reactive({
                name: '',
                id: '',
                type: '',
                description: '',
            });

            const dataSource = ref(data);

            const rules = {
                name: [{ required: true, message: 'Please enter user name' }],
                id: [{ required: true, message: 'please enter id' }],
                type: [{ required: true, message: 'Please choose the type' }],
            };

            const visible = ref<boolean>(false);

            const showDrawer = (key: number) => {
                visible.value = true;
                const tempData = dataSource.value.filter(item => item.key == key);
                if(tempData){
                    form.name = tempData[0].name;
                    form.id = tempData[0].id;
                    form.type = tempData[0].type;
                    // form.description = tempData[0].description;
                }
            };
            const onClose = () => {
                visible.value = false;
            };
            const onDelete = (key: number) => {
                dataSource.value = dataSource.value.filter(item => item.key !== key);
            };
            let i = 0;
            const handleAdd = (id: string) =>{

                const newData = {
                    key: i++,
                    name: form.name,
                    id:form.id,
                    type: form.type,
                };
                dataSource.value.push(newData);

                visible.value = false;
                form.name = '';
                form.id = '';
                form.type = '';
                form.description = '';
            };

            const handleChange = (info: FileInfo) => {
                if (info.file.status !== 'uploading') {
                    console.log(info.file, info.fileList);
                }
                if (info.file.status === 'done') {
                    message.success(`${info.file.name} file uploaded successfully`);
                } else if (info.file.status === 'error') {
                    message.error(`${info.file.name} file upload failed.`);
                }
            };

            const fileList = ref([]);

            return {
                dataSource,
                columns,
                editingKey: '',
                form,
                rules,
                visible,
                showDrawer,
                onClose,
                onDelete,
                handleAdd,

                fileList,
                headers: {
                    authorization: 'authorization-text',
                },
                handleChange,

            };
        },
    });
</script>
<style scoped>
    .editable-row-operations a {
        margin-right: 8px;
    }
    .inline-block {
        display: inline-block;
    }
</style>