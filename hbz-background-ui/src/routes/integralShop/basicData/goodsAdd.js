import React from 'react'
import { Modal, Form, Col, Row, Input, TreeSelect, Select, Button } from 'antd'
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
const GoodsAddModal = ({
  visible,
  form,
  dispatch,
  firmData,
  imgUrlSmall,
  imgUrlBig,
  wareTypeData,
  brandData,
  introductionData,
  loading,
  text,
}) => {
  const { getFieldDecorator } = form
  const handleSubmit = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        dispatch({ type: 'basicData/updateGoodsModal', payload: { introductionData: '', imgUrlBig: '', imgUrlSmall: '', text: true } })
        return
      }
      dispatch({ type: 'basicData/updateGoodsModal', payload: { loading: true } })
      if (firmData.name) {
        formValue.id = firmData.id
        dispatch({ type: 'basicData/updeteGoods', payload: formValue })
      } else {
        dispatch({ type: 'basicData/addGoods', payload: formValue })
      }
    })
  }
  // 关闭模态框
  const hideModal = () => {
    dispatch({ type: 'basicData/cleanGoodsModal' })
    form.resetFields()
  }
  // 重置
  const cleanFirm = () => {
    form.resetFields()
    dispatch({ type: 'basicData/updateGoodsModal', payload: { introductionData: '', imgUrlBig: '', imgUrlSmall: '', text: false } })
  }
  return (
    <div>
      <Modal
        title={firmData.id ? '修改商品' : '新增商品'}
        visible={visible}
        onOk={handleSubmit}
        maskClosable={false}
        onCancel={hideModal}
        width={800}
        className={styles.addModal}
        footer={null}
      >
        <Row style={{ fontSize: '18px', fontWeight: 'bold', textAlign: 'center' }} >
          {firmData.name && <span>商品编号：{firmData.wareNo}</span>}
        </Row>
        <Form>
          <Row>
            <Col span={12}>
              <FormItem label="商品名称" {...formItemLayout}>
                {getFieldDecorator('name', {
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
                  rules: [{ required: true, message: '请选择分类' }],
                })(
                  firmData.id ? <TreeSelect treeData={wareTypeData} disabled /> : <TreeSelect treeData={wareTypeData} />
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
                <Avatar form={form}
                  imgUrl={imgUrlSmall || firmData.header}
                  dispatch={dispatch}
                  type="basicData/updateGoodsModal"
                  name="imgUrlSmall"
                  nameForm="header"
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
                <Avatar
                  imgUrl={imgUrlBig || firmData.img}
                  dispatch={dispatch}
                  type="basicData/updateGoodsModal"
                  name="imgUrlBig"
                  nameForm="img"
                  form={form}
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
                  firmData.id ? <Select style={{ width: '100%' }} disabled >
                    {brandData && brandData.map(item => <Option key={item.id}>{item.name}</Option>)}
                  </Select>
                  : <Select style={{ width: '100%' }}>
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
                  firmData.id ? <Input disabled /> : <Input />
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
                  <Select style={{ width: '100%' }}>
                    <Option value={0} >停用</Option>
                    <Option value={1} >可用</Option>
                  </Select>
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
                  <Input />
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
                {getFieldDecorator('introduction', {
                  initialValue: introductionData || firmData.introduction,
                  rules: [{ required: true, message: '请填写商品详情' }],
                })(
                  <TextArea rows={4} style={{ display: 'none' }} />
                )}
                <Text textshow={text}
                  form={form}
                  getHtmlData={introductionData || firmData.introduction}
                  dispatch={dispatch}
                  types="basicData/updateGoodsModal"
                />
              </FormItem>
            </Col>
          </Row>
          {firmData.name && <div><Row>
            <Col span={12}>
              <FormItem label="商品创建人" {...formItemLayout}>
                {getFieldDecorator('createdBy', {
                  initialValue: firmData && firmData.createdBy,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="创建时间" {...formItemLayout}>
                {getFieldDecorator('createdDate', {
                  initialValue: firmData && firmData.createdDate,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="修改人" {...formItemLayout}>
                {getFieldDecorator('lastUpdatedBy', {
                  initialValue: firmData && firmData.lastUpdatedBy,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="修改时间" {...formItemLayout}>
                {getFieldDecorator('lastUpdatedDate', {
                  initialValue: firmData && firmData.lastUpdatedDate,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row></div>}
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
GoodsAddModal.propTypes = {
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
const ClassAddModalForm = Form.create()(GoodsAddModal)
export default ClassAddModalForm
