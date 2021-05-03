<template>
    <a-layout>
        <a-layout-content
                :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
            <a-button type="primary">物模型通信topic</a-button>
            <a-divider />

            <a-table :columns="columns" :data-source="dataSource" bordered style="word-break: break-all;">
                <template v-for="col in ['name', 'id', 'description']" #[col]="{ text }" :key="col">
                    <div>
                        {{ text }}
                    </div>
                </template>
                <template #operation="{ record }"  >
                    <a-tag @click="onClick(record.key)"> {{record.operation}} </a-tag>
                </template>
            </a-table>


        </a-layout-content>
    </a-layout>
</template>
<script lang="ts">
    import { defineComponent, reactive, ref, Ref, UnwrapRef } from 'vue';
    import axios from 'axios';
    import qs from 'query-string';
    import {notification} from "ant-design-vue";

    const columns = [
        {
            title: '功能',
            dataIndex: 'name',
            width: '10%',
            slots: { customRender: 'name' },
        },
        {
            title: 'Topic类',
            dataIndex: 'id',
            width: '50%',
            slots: { customRender: 'id' },
        },
        {
            title: '描述',
            dataIndex: 'description',
            width: '20%',
            slots: { customRender: 'description' },
        },
        {
            title: '操作权限',
            dataIndex: 'operation',
            slots: { customRender: 'operation' },
        },
    ];
    interface DataItem {
        key: number;
        name: string;
        id: string;
        description: string;
        operation: string;
    }
    const data: DataItem[] = [
        {
            key: 0,
            name: '属性上报',
            id: '/sys/${ProductKey}/${deviceName}/thing/event/property/post',
            description: '设备属性上报',
            operation: '发布'
        },
        {
            key: 1,
            name: '属性上报',
            id: '/sys/${ProductKey}/${deviceName}/thing/event/property/post_reply',
            description: '云端响应属性上报',
            operation: '订阅'
        },
        {
            key: 2,
            name: '属性设置',
            id: '/sys/${ProductKey}/${deviceName}/thing/service/property/set',
            description: '设备属性设置',
            operation: '订阅'
        },
    ];


    export default defineComponent({
        setup() {

            const dataSource = ref(data);
            const onClick = (key: number) => {
                const tempData = dataSource.value.filter(item => item.key == key);
                console.log(tempData[0]);
                axios.post("http://127.0.0.1:8880/topic",
                    tempData[0]
                ).then(
                    (response) => {
                        const data = response.data;
                        console.log(response);
                    });
                    notification.open({
                        message: 'Notification Title',
                        description:
                            'This is the content of the notification. This is the content of the notification. This is the content of the notification.',
                        onClick: () => {
                            console.log('Notification Clicked!');
                        },
                    });
            };
            return {
                dataSource,
                columns,
                editingKey: '',
                onClick,
                headers: {
                    authorization: 'authorization-text',
                },


            };
        },
    });
</script>
<style scoped>
    .editable-row-operations a {
        margin-right: 8px;
    }
</style>