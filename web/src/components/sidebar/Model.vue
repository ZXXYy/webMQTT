<template>
    <a-layout>
        <a-layout-content
                :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
            <a-list item-layout="vertical" size="large" :grid="{ gutter:20, column: 3}"
                    :data-source="listData">
                <template #renderItem="{ item }">
                    <a-list-item key="item.name">
                        <a-card style="width: 300px">
                            <template #title> {{ item.name }} </template>
                            <template #extra><a-tag color="blue">上传数据</a-tag></template>
                            <div>
                                {{ item.value }}
                            </div>
                            <p>{{item.date}}</p>
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

    const listData: any = [];
    for (let i = 0; i < 23; i++) {
        listData.push({
            name: '光强度',
            value: `${i}`,
            date: '2021-01-04',
        });
    }

    export default defineComponent({
        name: 'Home',
        setup(){
            console.log("setup");
            const ebooks = ref();
            // const ebooks1 = reactive({books:[]});

            onMounted(() => {
                console.log("onMounted");
                axios.get("http://127.0.0.1:8880/ebook/list?name=Spring").then(
                    (response) => {
                        const data = response.data;
                        ebooks.value = data.content;
                        // ebooks1.books = data.content;
                        console.log(response);
                    });
            });
            return {
                ebooks,
                // ebooks, ebooks2:toRef(ebooks1, "books")
                listData,
                pagination: {
                    onChange: (page: any) => {
                        console.log(page);
                    },
                    pageSize: 3,
                },
                actions:[
                    {type: 'StarOutlined', text:'156'},
                    {type: 'LikeOutlined', text:'156'},
                    {type: 'MessageOutlined', text:'2'},
                ],

            }
        }

    });
</script>
