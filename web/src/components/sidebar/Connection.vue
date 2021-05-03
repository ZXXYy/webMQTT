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
                        <check-circle style="float:right; margin-right:25%"/>
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
                        <a-button style="margin-left: 10px" @click="resetForm">Reset</a-button>
                    </a-form-item>
                </a-form>

        </a-layout-content>
    </a-layout>
</template>

<script lang="ts">
    import { ValidateErrorEntity } from 'ant-design-vue/es/form/interface';
    import { notification } from 'ant-design-vue';
    import { defineComponent, reactive, ref, toRaw, UnwrapRef } from 'vue';
    import axios from 'axios';
    import qs from 'query-string';
    interface FormState {
        addr: string;
        port: number;
        productKey: string;
        productSecret: string;
        deviceName: string;
        deviceSecret: string;
    }
    export default defineComponent({
        setup() {
            const formRef = ref();
            const formState: UnwrapRef<FormState> = reactive({
                addr: '',
                port: 1883,
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
                resetForm,
            };
        },
    });
</script>