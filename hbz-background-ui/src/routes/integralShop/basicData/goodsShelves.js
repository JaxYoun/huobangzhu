import React from 'react'
import { Modal, Form, Col, Row, Input, TreeSelect, Select, Button, InputNumber } from 'antd'
import PropTypes from 'prop-types'
import Avatar from '../common/upload'
import styles from '../common/index.less'
import Text from '../common/editor'

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
const GoodsShelevesModal = ({
  visible,
  form,
  dispatch,
  firmData,
  imgUrlSmall,
  imgUrlBig,
  wareTypeData,
  brandData,
  introductionData,
  text,
  loading,
}) => {
  const { getFieldDecorator } = form
  const handleSubmit = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({ type: 'basicData/updateGoodsShelvesModal', payload: { loading: true } })
      if (firmData.id) {
        formValue.wareId = firmData.id
        dispatch({ type: 'basicData/shelfUpGoods', payload: formValue })
      }
    })
  }
  // 关闭模态框
  const hideModal = () => {
    dispatch({ type: 'basicData/cleanGoodsShelvesModal' })
    form.resetFields()
  }
  // 重置
  const cleanFirm = () => {
    form.resetFields()
    dispatch({ type: 'basicData/updateGoodsShelvesModal', payload: { introductionData: '', imgUrlBig: '', imgUrlSmall: '', text: false } })
  }

  return (
    <div>
      <Modal
        title={'商品上架'}
        visible={visible}
        onOk={handleSubmit}
        maskClosable={false}
        onCancel={hideModal}
        width={800}
        className={styles.addModal}
        footer={null}
      >
        <Row style={{ fontSize: '18px', fontWeight: 'bold', textAlign: 'center' }} >
          {firmData.id && <span>商品编号：{firmData.wareNo}</span>}
        </Row>
        <Form>
          <Row>
            <Col span={12}>
              <FormItem label="商品上架名称" {...formItemLayout}>
                {getFieldDecorator('productName', {
                  initialValue: firmData && firmData.name,
                  rules: [{ required: true, message: '请填写商品名称' }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="商品类型" {...formItemLayout}>
                {getFieldDecorator('typeId', {
                  initialValue: firmData && firmData.typeId,
                  rules: [{ required: true, message: '请选择父分类' }],
                })(
                  <TreeSelect
                    disabled
                    treeData={wareTypeData}
                  />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="商品小图标" {...formItemLayout}>
              {getFieldDecorator('header', {
                initialValue: imgUrlSmall || firmData.header,
                rules: [{ required: true, message: '请选择图片!' }],
              })(
                <Input style={{ display: 'none' }} />
              )}
                <Avatar nameForm="header"
                  form={form}
                  imgUrl={imgUrlSmall || firmData.header}
                  dispatch={dispatch}
                  type="basicData/updateGoodsShelvesModal"
                  name="imgUrlSmall"
                />
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="商品大图标" {...formItemLayout}>
              {getFieldDecorator('img', {
                initialValue: imgUrlBig || firmData.img,
                rules: [{ required: true, message: '请选择图片!' }],
              })(
                <Input style={{ display: 'none' }} />
              )}
                <Avatar nameForm="img"
                  form={form}
                  imgUrl={imgUrlBig || firmData.img}
                  dispatch={dispatch}
                  type="basicData/updateGoodsShelvesModal"
                  name="imgUrlBig"
                />
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="商品品牌" {...formItemLayout}>
                {getFieldDecorator('brandId', {
                  initialValue: firmData.id ? `${firmData.brandId}` : '',
                  rules: [{ required: true, message: '请填写商品品牌' }],
                })(
                  <Select style={{ width: '100%' }} disabled>
                      {brandData && brandData.map(item => <Option key={item.id}>{item.name}</Option>)}
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="商品规格" {...formItemLayout}>
                {getFieldDecorator('specifications', {
                  initialValue: firmData && firmData.specifications,
                  rules: [{ required: true, message: '请填写商品规格' }],
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="参考售价" {...formItemLayout}>
                {getFieldDecorator('marketAmount', {
                  initialValue: firmData && firmData.marketAmount,
                  rules: [{ required: true, message: '请填写参考售价为数字', pattern: RegExp('^\\d+(\\.\\d+)?$') }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="商品状态" {...formItemLayout}>
                {getFieldDecorator('state', {
                  initialValue: firmData && firmData.state,
                  rules: [{ required: true, message: '请填写商品状态' }],
                })(
                  <Select style={{ width: '100%' }} disabled>
                    <Option value={0} >停用</Option>
                    <Option value={1} >可用</Option>
                  </Select>
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="商品上架状态" {...formItemLayout}>
                {getFieldDecorator('productStatus', {
                  initialValue: 1,
                  rules: [{ required: true, message: '请填写上架状态' }],
                })(
                  <Select style={{ width: '100%' }} disabled>
                    <Option value={0} >下架</Option>
                    <Option value={1} >上架</Option>
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="兑换积分" {...formItemLayout}>
                {getFieldDecorator('score', {
                  initialValue: firmData && firmData.score,
                  rules: [{ required: true, message: '请填写积分', pattern: RegExp('^\\d+(\\.\\d+)?$') }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="上架数量" {...formItemLayout}>
                {getFieldDecorator('total', {
                  initialValue: firmData && firmData.total,
                  rules: [{ required: true, message: '上架数量为非0正整数' }],
                })(
                  <InputNumber min={0} precision={0} />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="剩余数量" {...formItemLayout}>
                {getFieldDecorator('leave', {
                  initialValue: firmData && firmData.leave,
                  rules: [{ required: true, message: '剩余数量为非0正整数' }],
                })(
                  <InputNumber min={0} precision={0} />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <FormItem label="产地" {...formItemLayouts}>
                {getFieldDecorator('productionAddress', {
                  initialValue: firmData && firmData.productionAddress,
                  rules: [{ required: true, message: '请填写产地' }],
                })(
                  <Input disabled title={firmData && firmData.productionAddress} />
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
            <Col span={24}>
              <FormItem label="商品详情" {...formItemLayouts}>
                {getFieldDecorator('content', {
                  initialValue: introductionData || firmData.introduction,
                  rules: [{ required: true, message: '请填写商品详情' }],
                })(
                  <TextArea rows={2} style={{ display: 'none' }} />
                )}
                <Text textshow
                  form={form}
                  getHtmlData={introductionData || firmData.introduction}
                  dispatch={dispatch}
                  types="basicData/updateGoodsShelvesModal"
                />
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <FormItem label="备注" {...formItemLayouts}>
                {getFieldDecorator('message', {
                  initialValue: firmData && firmData.message,
                  rules: [{ required: true, message: '请填写商品简述' }],
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
GoodsShelevesModal.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  wareTypeData: PropTypes.arry,
  imgUrlSmall: PropTypes.string,
  imgUrlBig: PropTypes.string,
  firmData: PropTypes.obj,
  brandData: PropTypes.array,
  introductionData: PropTypes.string,
  text: PropTypes.bool,
  loading: PropTypes.bool,
}
const GoodsShelevesModalForm = Form.create()(GoodsShelevesModal)
export default GoodsShelevesModalForm
