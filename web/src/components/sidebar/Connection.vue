<template>
    <a-layout>
        <a-layout-content
                :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
                <a-form
                        ref="formRef"
                        :model="formState"
                        :rules="rules"
                        :label-col="labelCol"
                        :wrapper-col="wrapperCol"
                >
                    <div>
                        <a-button type="primary">MQTT Broker Profile Settings</a-button>
                        <a-badge v-if="connected" color="#87d068 " text="已连接" style="float:right; margin-right:25%"/>
                        <a-badge v-else color="#7E8B92" text="未连接" style="float:right; margin-right:25%"/>
                    </div>

                    <a-divider />
                    <a-form-item ref="addr" label="Broker Address" name="addr">
                        <a-input v-model:value="formState.addr" />
                    </a-form-item>
                    <a-form-item ref="port" label="Broker Port" name="port" placeholder="1883">
                        <a-input v-model:value="formState.port" />
                    </a-form-item>


                    <a-button type="primary">Users Credentials</a-button>
                    <a-divider />
                    <a-form-item ref="productKey" label="Product Key" name="productKey">
                        <a-input v-model:value="formState.productKey" />
                    </a-form-item>
                    <a-form-item ref="productSecret" label="Product Secret" name="productSecret">
                        <a-input v-model:value="formState.productSecret" type="productSecret" />
                    </a-form-item>
                    <a-form-item ref="deviceName" label="Device Name" name="deviceName">
                        <a-input v-model:value="formState.deviceName" type="deviceName" />
                    </a-form-item>
                    <a-form-item ref="deviceSecret" label="Device Secret" name="deviceSecret">
                        <a-input v-model:value="formState.deviceSecret" type="deviceSecret" />
                    </a-form-item>

                    <a-form-item :wrapper-col="{ span: 14, offset: 4 }">
                        <a-button type="primary" @click="onSubmit">Connect</a-button>
                        <a-button style="margin-left: 10px" type="primary" @click="onDisconnet">Disconnect</a-button>
                        <a-button style="margin-left: 10px" @click="resetForm">Reset</a-button>
                    </a-form-item>
                </a-form>

        </a-layout-content>
    </a-layout>
</template>

<script lang="ts">
    import { ValidateErrorEntity } from 'ant-design-vue/es/form/interface';
    import { notification, message } from 'ant-design-vue';
    import { defineComponent, reactive, ref, toRaw, UnwrapRef } from 'vue';
    import axios from 'axios';
    import qs from 'query-string';
    interface FormState {
        addr: string;
        port: string;
        productKey: string;
        productSecret: string;
        deviceName: string;
        deviceSecret: string;
    }
    export default defineComponent({
        setup() {
            const connected = ref();
            const formRef = ref();
            const formState: UnwrapRef<FormState> = reactive({
                addr: '',
                port: '',
                productKey: '',
                productSecret: '',
                deviceName: '',
                deviceSecret: '',
            });
            const rules = {
                addr: [
                    { required: true, message: 'Please input Broker Address', trigger: 'blur' }
                ],
                port: [{ required: true, message: 'Please input Broker port', trigger: 'blur' }],
                productKey: [{ required: true, message: 'Please input productKey', trigger: 'blur'}],
                productSecret: [{required: true, message: 'Please input productSecret', trigger: 'blur'}],
                deviceName: [{required: true, message: 'Please input deviceName', trigger: 'blur'}],
                deviceSecret: [{required: true, message: 'Please input deviceSecret', trigger: 'blur'}],

            };

            const onDisconnet = () => {
                axios.post("http://127.0.0.1:8880/disconnect",
                    "disconnect"
                ).then(
                    (response) => {
                        const data = response.data;
                        if(data=='Success'){
                            connected.value = false;
                            message.success('成功断开连接');
                        }
                        else{
                            connected.value = true;
                            message.error('断开连接失败');
                        }
                    }
                );
            }
            const onSubmit = () => {
                formRef.value
                    .validate()
                    .then(() => {
                        console.log('values', formState, toRaw(formState));
                        axios.post("http://127.0.0.1:8880/connect",
                            formState
                        ).then(
                            (response) => {
                                const data = response.data;
                                if(data=='Success'){
                                    connected.value = true;
                                    message.success('连接成功');
                                }
                                else if(data=='Fail'){
                                    connected.value = false;
                                    message.error('用户名或密码错误');
                                }
                                else{
                                    connected.value = false;
                                    message.error('拒绝连接');
                                }
                            });
                    })
                    .catch((error: ValidateErrorEntity<FormState>) => {
                        console.log('error', error);
                    });
            };
            const resetForm = () => {
                formRef.value.resetFields();
            };
            return {
                formRef,
                labelCol: { span: 4 },
                wrapperCol: { span: 14 },
                other: '',
                formState,
                rules,
                onSubmit,
                onDisconnet,
                resetForm,
                connected,
            };
        },
    });
</script>