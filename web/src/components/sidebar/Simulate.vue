<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-table :columns="columns" :data-source="dataSource" bordered>
        <template v-for="col in ['name', 'id', 'type']" #[col]="{ text }" :key="col">
          <div>
            {{ text }}
          </div>
        </template>

        <template #operation="{ record }">
          <a-input v-model:value="record.interval" suffix="/s" style="width: 100px"/>
          <a-switch @change="onChange(record.id)" v-model:checked="record.checked" style="margin-left: 5%"/>
        </template>
      </a-table>


    </a-layout-content>
  </a-layout>
</template>
<script lang="ts">
import { message } from 'ant-design-vue';
import {defineComponent, onMounted, ref, watch} from 'vue';
import axios from "axios";
import moment, { Moment } from 'moment';

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
    width: '20%',
    slots: { customRender: 'type' },
  },
  {
    title: '定时操作',
    dataIndex: 'operation',
    slots: { customRender: 'operation' },
  },
];
interface DataItem {
  key: number;
  name: string;
  id: string;
  type: string;
  checked: boolean;
  interval: number;

}
const data: DataItem[] = [];

export default defineComponent({
  setup() {
    const dataSource = ref(data);
    // const value = ref<Moment>(moment('00:00:00', 'HH:mm:ss'));
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
    const onChange = (id: string) =>{
      const tempData = dataSource.value.filter(item => item.id == id);
      const tempData2: DataItem = {
        key: tempData[0].key,
        id: tempData[0].id,
        type: tempData[0].type,
        name: tempData[0].name,
        checked: !tempData[0].checked,
        interval: tempData[0].interval
      };
      axios.post("http://127.0.0.1:8880/timer",
          tempData2
      ).then(
          (response) => {
            const data = response.data;
            if(data=='Success'){
              message.success('设置成功');
              refresh();
            }
            else{
              message.error('设置失败');
            }
          }
      );
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

    return {
      dataSource,
      columns,
      onChange,

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