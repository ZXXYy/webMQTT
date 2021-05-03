<template>
    <a-layout>
        <a-layout-content
                :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
            <a-list item-layout="vertical" size="large" :grid="{ gutter:20, column: 3}"
                    :data-source="dataSource">
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
                                    <a-popover :key="item.name" trigger="click">
                                        <template #title>{{item.name}}</template>
                                        <template #content>
                                            <a-input-search
                                                  v-model:value="newValue"
                                                  placeholder="请输入值"
                                                  @search="onSearch(item.key)"
                                              >
                                              <template #enterButton>
                                                <a-button>上传</a-button>
                                              </template>
                                            </a-input-search>
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

    interface DataItem {
      key: number;
      name: string;
      value: string;
      date: string;
    }
    const data: DataItem[] = [];

    export default defineComponent({
        name: 'Home',
        setup(){
            console.log("setup");
            const dataSource = ref(data);
            const newValue = ref<string>('');
            const onSearch = (key: number) => {
                console.log('or use this.value', newValue.value);
                const tempData = dataSource.value.filter(item => item.key == key);
                if(newValue.value!=''){
                  tempData[0].value = newValue.value;
                  axios.post("http://127.0.0.1:8880/model",
                      tempData[0]
                  ).then(
                  );
                }
            };

            const visible = ref<boolean>(false);

          onMounted(() => {
            axios.get("http://127.0.0.1:8880/attribute",
            ).then(
                (response) => {
                  const data = response.data["a"];
                  console.log(data);
                  dataSource.value = data;
                }
            );
          });
            const hide = () => {
                visible.value = false;
            };

            return {
              dataSource,
                pagination: {
                    onChange: (page: any) => {
                        console.log(page);
                    },
                    pageSize: 3,
                },
                hide,
                visible,
                newValue,
                onSearch
            }
        }

    });
</script>
