import React from 'react'
import { Modal, Form, Col, Row, Input, TreeSelect, Button, InputNumber } from 'antd'
import PropTypes from 'prop-types'
import Avatar from '../common/upload'
import styles from '../common/index.less'

const FormItem = Form.Item
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

const ClassAddModal = ({
  visible,
  form,
  dispatch,
  firmData,
  imgUrl,
  wareTypeData,
  goodslevel,
  loading,
}) => {
  const { getFieldDecorator } = form
  const handleSubmit = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        console.log(formValue)
        return
      }
      dispatch({ type: 'basicData/updateClassificationModal', payload: { loading: true } })
      if (firmData.id) {
        formValue.id = firmData.id
        dispatch({ type: 'basicData/updateClass', payload: formValue })
      } else {
        dispatch({ type: 'basicData/addClass', payload: formValue })
      }
    })
  }
  // 关闭模态框
  const hideModal = () => {
    dispatch({ type: 'basicData/cleanClassificationModal' })
    form.resetFields()
  }
  // 获取商品分类级别
  const treeChange = (data, label, extra) => {
    goodslevel = parseInt(extra.triggerNode.props.level + 1)
    form.setFieldsValue({ level: goodslevel })
  }
  // 重置
  const cleanFirm = () => {
    form.resetFields()
    dispatch({ type: 'basicData/updateClassificationModal', payload: { goodslevel: '', imgUrl: '' } })
  }
  return (
    <div>
      <Modal
        title={firmData.id ? '修改商品分类' : '新增商品分类'}
        visible={visible}
        maskClosable={false}
        width={600}
        className={styles.addModal}
        footer={null}
        onCancel={hideModal}
      >
        <Form>
          <Row>
            <Col span={24}>
              <FormItem label="商品分类名称" {...formItemLayout}>
                {getFieldDecorator('name', {
                  initialValue: firmData && firmData.name,
                  rules: [{ required: true, message: '请填写商品分类' }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <FormItem label="父分类" {...formItemLayout}>
                {getFieldDecorator('parentId', {
                  initialValue: firmData && firmData.parentId,
                  rules: [{ required: false, message: '请选择父分类' }],
                })(
                  <TreeSelect
                    placeholder="若为一级分类则不选"
                    treeData={wareTypeData}
                    onChange={treeChange}
                  />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <FormItem label="商品分类图标" {...formItemLayout}>
              {getFieldDecorator('headerBit', {
                initialValue: imgUrl || firmData.headerBit,
                rules: [{ required: true, message: '请选择图片!' }],
              })(
                <Input style={{ display: 'none' }} />
              )}
                <Avatar form={form}
                  imgUrl={imgUrl || firmData.headerBit}
                  dispatch={dispatch}
                  type="basicData/updateClassificationModal"
                  name="imgUrl"
                  nameForm="headerBit"
                />
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <FormItem label="商品分类级别" {...formItemLayout}>
                {getFieldDecorator('level', {
                  initialValue: firmData.level ? Number(firmData.level) : Number(goodslevel),
                  rules: [{ required: true, message: '请选择分类级别,且级别小于4', pattern: RegExp('^[0-4]{1}') }],
                })(
                  <Input type="number" disabled />
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
ClassAddModal.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  wareTypeData: PropTypes.arry,
  imgUrl: PropTypes.string,
  firmData: PropTypes.obj,
  goodslevel: PropTypes.number,
  loading: PropTypes.bool,
}
const ClassAddModalForm = Form.create()(ClassAddModal)
export default ClassAddModalForm
