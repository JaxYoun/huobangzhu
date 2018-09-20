import React from 'react'
import { Modal, Form, Col, Row, Input, Select, InputNumber, Button, TreeSelect } from 'antd'
import PropTypes from 'prop-types'
import styles from '../common/index.less'
import Avatar from '../common/upload'

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
const RecommonModalUpdate = ({
  visible,
  form,
  dispatch,
  firmData,
  loading,
  imgUrl,
  wareTypeData,
}) => {
  const { getFieldDecorator } = form
  // 关闭模态框
  const hideModal = () => {
    dispatch({ type: 'scoreExchange/cleanupRecommonModalUpdate' })
    form.resetFields()
  }
  // 提交
  const handleSubmit = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({ type: 'scoreExchange/updateRecommonModalUpdate', payload: { loading: true } })
      formValue.id = firmData.id
      formValue.productId = firmData.product.id
      dispatch({ type: 'scoreExchange/updateRecommons', payload: formValue })
      form.resetFields()
    })
  }
  return (
    <div>
      <Modal
        title={'商品推荐'}
        visible={visible}
        onOk={handleSubmit}
        maskClosable={false}
        onCancel={hideModal}
        width={800}
        className={styles.exchangeModal}
        footer={null}
      >
        <Form>
          <Row>
            <Col span={12}>
              <FormItem label="商品编码" {...formItemLayout}>
                {getFieldDecorator('wareNo', {
                  initialValue: firmData && firmData.product.ware.wareNo,
                  rules: [{ required: true, message: '请填写排序' }],
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="商品上架名称" {...formItemLayout}>
                {getFieldDecorator('productName', {
                  initialValue: firmData && firmData.product.ware.name,
                  rules: [{ required: true, message: '请填写商品名称' }],
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="商品类型" {...formItemLayout}>
                {getFieldDecorator('typeId', {
                  initialValue: (firmData && firmData.product && firmData.product.ware) && firmData.product.ware.typeId,
                  rules: [{ required: false, message: '请选择父分类' }],
                })(
                  <TreeSelect
                    disabled
                    treeData={wareTypeData}
                  />
                )}
              </FormItem>
            </Col>
          </Row>
          <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed  #000' }} />
          <Row>
            <Col span={12}>
              <FormItem label="排序" {...formItemLayout}>
                {getFieldDecorator('index', {
                  initialValue: firmData && firmData.index,
                  rules: [{ required: true, message: '请填写排序' }],
                })(
                  <InputNumber />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="推荐位置" {...formItemLayout}>
              {getFieldDecorator('useType', {
                initialValue: firmData && firmData.useType,
                rules: [{ required: true, message: '请选择推荐位置!' }],
              })(
                <Select style={{ width: '100%' }}>
                  <Option key={1} >热门</Option>
                  <Option key={2} >横幅</Option>
                </Select>
              )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <FormItem label="商品简述" {...formItemLayouts}>
                {getFieldDecorator('summary', {
                  initialValue: firmData && firmData.summary,
                  rules: [{ required: true, message: '请填写商品简述' }],
                })(
                  <TextArea rows={2} />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="图片" {...formItemLayout}>
              {getFieldDecorator('headerBit', {
                rules: [{ required: true, message: '请选择图片!' }],
                initialValue: imgUrl || firmData.headerBit,
              })(
                <Input style={{ display: 'none' }} />
              )}
                <Avatar form={form}
                  imgUrl={imgUrl || firmData.headerBit}
                  dispatch={dispatch}
                  type="scoreExchange/updateRecommonModalUpdate"
                  name="imgUrl"
                  nameForm="headerBit"
                />
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <FormItem label="备注" {...formItemLayouts}>
                {getFieldDecorator('introduce', {
                  initialValue: firmData && firmData.introduce,
                  rules: [{ required: true, message: '请填写商品备注' }],
                })(
                  <TextArea rows={2} />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row className={styles.buttonPosition}>
            <Button type="primary" onClick={handleSubmit.bind(this)} loading={loading}>确定</Button>
            <Button type="primary" onClick={hideModal.bind(this)}>取消</Button>
          </Row>
        </Form>
      </Modal>
    </div>
  )
}
RecommonModalUpdate.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  firmData: PropTypes.obj,
  loading: PropTypes.bool,
  imgUrl: PropTypes.string,
  wareTypeData: PropTypes.array,
}
const RecommonModalUpdateForm = Form.create()(RecommonModalUpdate)
export default RecommonModalUpdateForm
