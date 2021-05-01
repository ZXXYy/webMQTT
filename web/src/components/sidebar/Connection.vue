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
                    <a-button type="primary">MQTT Broker Profile Settings</a-button>
                    <a-divider />
                    <a-form-item ref="addr" label="Broker Address" name="addr">
                        <a-input v-model:value="formState.addr" />
                    </a-form-item>
                    <a-form-item ref="port" label="Broker Port" name="port" placeholder="1883">
                        <a-input v-model:value="formState.port" />
                    </a-form-item>
                    <a-form-item ref="id" label="Client ID" name="id">
                        <a-input v-model:value="formState.id" />
                    </a-form-item>

                    <a-button type="primary">Users Credentials</a-button>
                    <a-divider />
                    <a-form-item ref="user" label="User Name" name="user">
                        <a-input v-model:value="formState.user" />
                    </a-form-item>
                    <a-form-item ref="pwd" label="Password" name="pwd">
                        <a-input v-model:value="formState.pwd" type="password" />
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
    import { Moment } from 'moment';
    import { defineComponent, reactive, ref, toRaw, UnwrapRef } from 'vue';
    interface FormState {
        addr: string;
        port: number;
        id: string;
        user: string;
        pwd: string;
    }
    export default defineComponent({
        setup() {
            const formRef = ref();
            const formState: UnwrapRef<FormState> = reactive({
                addr: '',
                port: 1883,
                id: '',
                user: '',
                pwd: '',

            });
            const rules = {
                addr: [
                    { required: true, message: 'Please input Broker Address', trigger: 'blur' }
                ],
                port: [{ required: true, message: 'Please input Broker port', trigger: 'blur' }],
                id: [{ required: true, message: 'Please input client ID', trigger: 'blur'}],
                user: [{required: true, message: 'Please input user name', trigger: 'blur'}],
                pwd: [{required: true, message: 'Please input password', trigger: 'blur'}],

            };
            const onSubmit = () => {
                formRef.value
                    .validate()
                    .then(() => {
                        console.log('values', formState, toRaw(formState));
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