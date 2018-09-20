import React from 'react'
import { Modal, Form, Col, Row, Input, Select, Button } from 'antd'
import PropTypes from 'prop-types'
import styles from '../index.less'
const FormItem = Form.Item
const { TextArea } = Input
const formItemLayout = {
  labelCol: {
    xs: { span: 6 },
    sm: { span: 6 },
  },
  wrapperCol: {
    xs: { span: 18 },
    sm: { span: 18 },
  },
}
const formItemLayouts = {
  labelCol: {
    xs: { span: 3 },
    sm: { span: 3 },
  },
  wrapperCol: {
    xs: { span: 21 },
    sm: { span: 21 },
  },
}
const UserModalForm = ({
  visible,
  form,
  dispatch,
  firmData,
  userClassification,
  bankData,
  loading,
}) => {
  const { getFieldDecorator } = form
  const handleSubmit = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({ type: 'userManagement/updatefirmModal', payload: { loading: true } })
      if (firmData.id) {
        formValue.id = firmData.id
        dispatch({ type: 'userManagement/updateUsers', payload: formValue })
      } else {
        dispatch({ type: 'userManagement/addUsers', payload: formValue })
      }
    })
  }
  // 关闭模态框
  const hideModal = () => {
    dispatch({ type: 'userManagement/cleanModal' })
    form.resetFields()
  }
  // 重置
  const cleanFirm = () => {
    form.resetFields()
  }

  return (
    <div>
      <Modal
        title={'客户信息'}
        visible={visible}
        onOk={handleSubmit}
        maskClosable={false}
        onCancel={hideModal}
        width={800}
        footer={null}
      >
        <Row style={{ fontSize: '18px', fontWeight: 'bold', textAlign: 'center' }} >
          {firmData.name && <span>商品编号：{firmData.wareNo}</span>}
        </Row>
        <Form>
          <Row>
            <Col span={12}>
              <FormItem label="单位名称" {...formItemLayout}>
                {getFieldDecorator('companyName', {
                  initialValue: firmData && firmData.companyName,
                  rules: [{ required: true, message: '请填写单位名称(20个字以下)', max: 19 }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="客户名称" {...formItemLayout}>
                {getFieldDecorator('userName', {
                  initialValue: firmData && firmData.userName,
                  rules: [{ required: true, message: '请填写客户名称（20个字以下）', max: 19 }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="客户分类" {...formItemLayout}>
                {getFieldDecorator('userClassification', {
                  initialValue: firmData && firmData.userClassification,
                  rules: [{ required: true, message: '请选择客户分类' }],
                })(
                  <Select style={{ width: '100%' }} >
                      {userClassification && userClassification.map(item => <Option key={item.val}>{item.name}</Option>)}
                  </Select>
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="手机号码" {...formItemLayout}>
                {getFieldDecorator('userTelephone', {
                  initialValue: firmData && firmData.userTelephone,
                  rules: [{ required: true, message: '请填写正确的手机号码', pattern: RegExp('^0\\d{2,3}-?\\d{7,8}$|^(13[0-9]|15[0-9]|18[0-9]|14[0-9]|17[0-9])\\d{8}$') }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="身份证号" {...formItemLayout}>
                {getFieldDecorator('idCard', {
                  initialValue: firmData && firmData.idCard,
                  rules: [{ required: true, message: '请填写正确的身份证号', pattern: RegExp('^[1-9]\\d{5}[1-9]\\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2]\\d)|(3[0-1]))((\\d{4})|(\\d{3}[Xx]))$') }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="地址" {...formItemLayout}>
                {getFieldDecorator('userAddress', {
                  initialValue: firmData && firmData.userAddress,
                  rules: [{ required: true, message: '请填写地址' }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="开户行" {...formItemLayout}>
                {getFieldDecorator('bank', {
                  initialValue: firmData && firmData.bank,
                  rules: [{ required: true, message: '请选择开户行' }],
                })(
                  <Select style={{ width: '100%' }} >
                      {bankData && bankData.map(item => <Option key={item.val}>{item.name}</Option>)}
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="银行账号" {...formItemLayout}>
                {getFieldDecorator('bankAccount', {
                  initialValue: firmData && firmData.bankAccount, max: 20,
                  rules: [{ required: true, message: '请填写银行账号' }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="简拼" {...formItemLayout}>
                {getFieldDecorator('jianpin', {
                  initialValue: firmData && firmData.jianpin,
                  rules: [{ required: true, message: '请填写简拼' }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <FormItem label="备注" {...formItemLayouts}>
                {getFieldDecorator('remarks', {
                  initialValue: firmData && firmData.remarks,
                  rules: [{ required: false, message: '请填写商品简述' }],
                })(
                  <TextArea rows={2} />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row className={styles.buttonPosition}>
            <Button type="primary" onClick={handleSubmit.bind(this)} loading={loading}>确定</Button>
            <Button type="primary" onClick={hideModal.bind(this)}>取消</Button>
            {firmData.id ? '' : <Button type="danger" onClick={cleanFirm.bind(this)} >重置</Button>}
          </Row>
        </Form>
      </Modal>
    </div>
  )
}
UserModalForm.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  firmData: PropTypes.obj,
  userClassification: PropTypes.array,
  bankData: PropTypes.array,
  loading: PropTypes.bool,
}
const UserModal = Form.create()(UserModalForm)
export default UserModal
