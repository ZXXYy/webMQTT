<template>
    <a-layout>
        <a-layout-content
                :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
            <a-list item-layout="vertical" size="large" :grid="{ gutter:20, column: 3}"
                    :data-source="listData">
                <template #renderItem="{ item }">
                    <a-list-item key="item.name">
                        <a-card style="width: 270px;border: 1px solid #f2f2f2;
                                        box-shadow: 0 1px 4px 0 rgba(0,0,0,.08);
                                        height:124px;
                                        " hoverable
                                bodyStyle="padding-top:15px">
                            <div>
                                <span style="font-size: 14px;color: #333; margin:0px">
                                    {{item.name}}
                                </span>
                                <span>
                                    <a-popover v-model:visible="visible" title="Title" trigger="click">
                                        <template #content>
                                          <a @click="onClick(item.key)">Close</a>
                                        </template>
                                        <a-button type="link" style="float: right" >上传数据</a-button>
                                    </a-popover>
                                </span>
                            </div>
                            <p style="font-size: 28px;color: #333; margin:0px">
                                {{ item.value }}
                            </p>
                            <p style="color: #999;font-size: 14px;">
                                {{item.date}}
                            </p>
                        </a-card>
                    </a-list-item>
                </template>
            </a-list>

        </a-layout-content>
    </a-layout>
</template>
<script lang="ts">
    import { defineComponent, onMounted, ref, reactive, toRef } from 'vue';
    import axios from 'axios';
    import qs from "query-string";
    import {notification} from "ant-design-vue";

    const listData: any = [];
    for (let i = 0; i < 10; i++) {
        listData.push({
            key: i,
            name: '光强度',
            id: `${i}`,
            value: `${i}`,
            date: '2021-01-04',
        });
    }

    export default defineComponent({
        name: 'Home',
        setup(){
            console.log("setup");
            const onClick = (key: number) => {
                const tempData = listData.filter((listData: { key: number; }) => listData.key == key);

                axios.post("http://127.0.0.1:8080/model",
                    qs.stringify(tempData)
                ).then(
                    (response) => {
                        const data = response.data;
                        console.log(response);
                    });
                // notification.open({
                //     message: 'Notification Title',
                //     description:
                //         'This is the content of the notification. This is the content of the notification. This is the content of the notification.',
                //     onClick: () => {
                //         console.log('Notification Clicked!');
                //     },
                // });
            };

            const visible = ref<boolean>(false);

            const hide = () => {
                visible.value = false;
            };

            return {
                listData,
                pagination: {
                    onChange: (page: any) => {
                        console.log(page);
                    },
                    pageSize: 3,
                },
                onClick,
                hide,
                visible,

            }
        }

    });
</script>
