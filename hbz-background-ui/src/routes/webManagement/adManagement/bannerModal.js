import React from 'react'
import { Modal, Form, Col, Row, Input, Select, Button, InputNumber } from 'antd'
import PropTypes from 'prop-types'
import Avatar from '../common/upload'
import styles from '../common/index.less'

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
const BannerModal = ({
  visible,
  form,
  dispatch,
  firmData,
  imgUrlSmall,
  locationData,
  loading,
  skipTypeData,
}) => {
  const { getFieldDecorator } = form
  const handleSubmit = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        dispatch({ type: 'adManagement/updateBannerModal', payload: { imgUrlSmall: '', text: true } })
        return
      }
      dispatch({ type: 'adManagement/updateBannerModal', payload: { loading: true } })
      if (firmData.id) {
        formValue.id = firmData.id
        dispatch({ type: 'adManagement/updateBanners', payload: formValue })
      } else {
        dispatch({ type: 'adManagement/addBanner', payload: formValue })
      }
    })
  }
  // 关闭模态框
  const hideModal = () => {
    dispatch({ type: 'adManagement/cleanBannerModal' })
    form.resetFields()
  }
  // 重置
  const cleanFirm = () => {
    form.resetFields()
    dispatch({ type: 'adManagement/updateBannerModal', payload: { imgUrlSmall: '', text: false } })
  }
  return (
    <div>
      <Modal
        title={firmData.id ? '修改Banner信息' : '新增Banner信息'}
        visible={visible}
        onOk={handleSubmit}
        maskClosable={false}
        onCancel={hideModal}
        width={800}
        className={styles.addModal}
        footer={null}
      >
        <Row style={{ fontSize: '18px', fontWeight: 'bold', textAlign: 'center' }} >
          {firmData.id && <span>编号：{firmData.code}</span>}
        </Row>
        <Form>
          <Row>
            <Col span={12}>
              <FormItem label="Banner名称" {...formItemLayout}>
                {getFieldDecorator('name', {
                  initialValue: firmData && firmData.name,
                  rules: [{ required: true, message: '请填写Banner名称(长度不大于100)', max: 99 }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="Banner位置" {...formItemLayout}>
                {getFieldDecorator('location', {
                  initialValue: (firmData && firmData.location) && firmData.location.val,
                  rules: [{ required: true, message: '请选择Banner位置' }],
                })(
                  <Select style={{ width: '100%' }}>
                   {locationData && locationData.map(item => <Option key={item.val}>{item.name}</Option>)}
                  </Select>
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="跳转方式" {...formItemLayout}>
                {getFieldDecorator('skipType', {
                  initialValue: (firmData && firmData.skipType) && firmData.skipType.val,
                  rules: [{ required: true, message: '请填写跳转方式' }],
                })(
                  <Select style={{ width: '100%' }}>
                   {skipTypeData && skipTypeData.map(item => <Option key={item.val}>{item.name}</Option>)}
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="排序" {...formItemLayout}>
                {getFieldDecorator('sortNo', {
                  initialValue: firmData && firmData.sortNo,
                  rules: [{ required: true, message: '请填写排序' }],
                })(
                  <InputNumber />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <FormItem label="跳转url地址" {...formItemLayouts}>
                {getFieldDecorator('url', {
                  initialValue: firmData && firmData.url,
                  rules: [{ required: false, message: '请填写跳转url地址' }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="Banner状态" {...formItemLayout}>
                {getFieldDecorator('ifEnable', {
                  initialValue: firmData && firmData.ifEnable,
                  rules: [{ required: true, message: '请填写跳转方式' }],
                })(
                  <Select style={{ width: '100%' }}>
                    <Option key="0" >已禁用</Option>
                    <Option key="1" >可用</Option>
                  </Select>
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="Bannner图片" {...formItemLayout}>
              {getFieldDecorator('imagePath', {
                initialValue: imgUrlSmall || firmData.imagePath,
                rules: [{ required: true, message: '请选择图片!' }],
              })(
                <Input style={{ display: 'none' }} />
              )}
                <Avatar form={form}
                  imgUrl={imgUrlSmall || firmData.imagePath}
                  dispatch={dispatch}
                  type="adManagement/updateBannerModal"
                  name="imgUrlSmall"
                  nameForm="imagePath"
                />
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <FormItem label="备注" {...formItemLayouts}>
                {getFieldDecorator('remark', {
                  initialValue: firmData && firmData.remark,
                  rules: [{ required: false, message: '请填写备注' }],
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
BannerModal.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  locationData: PropTypes.arry,
  skipTypeData: PropTypes.arry,
  imgUrlSmall: PropTypes.string,
  firmData: PropTypes.obj,
  loading: PropTypes.bool,
}
const BannerModalForm = Form.create()(BannerModal)
export default BannerModalForm
