<template>
    <a-layout>
        <a-layout-content
                :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
          <a-button @click="onClick" size="small" style="margin-bottom: 1%">
            <template #icon><SyncOutlined /></template>
          </a-button>
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
                                    <a-popover  :key="item.name" trigger="click">
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
                                        <a-button :key="item.name" type="link" style="float: right">上传数据</a-button>
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
    import {message, notification} from "ant-design-vue";

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
            const visible = ref<boolean>(false);

            const onSearch = (key: number) => {
                console.log('or use this.value', newValue.value);
                const tempData = dataSource.value.filter(item => item.key == key);
                const tempData2: DataItem = {
                  key: tempData[0].key,
                  name: tempData[0].name,
                  value: tempData[0].value,
                  date: tempData[0].date
                };

                if(newValue.value!=''){
                  tempData2.value = newValue.value;
                  axios.post("http://127.0.0.1:8880/model",
                      tempData2
                  ).then(
                      (response) => {
                        const data = response.data;
                        const status = response.status;
                        if(status!=200){
                          message.error('服务器错误');
                        }
                        else if(data=='Success'){
                          message.success('上传成功');
                          refresh();
                        }
                        else{
                          message.error('上传失败');
                        }
                      }
                  );
                }

            };
          const onClick = () => {
            refresh();
          }
            const refresh = () =>{
              axios.get("http://127.0.0.1:8880/attribute",
              ).then(
                  (response) => {
                    const data = response.data["a"];
                    console.log(data);
                    dataSource.value = data;
                  }
              );
            }


          onMounted(() => {
            refresh();
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
                onSearch,
                onClick
            }
        }

    });
</script>
