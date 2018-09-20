import React from 'react'
import { Form, Input, Row, Col, Button, Card, TreeSelect, Select } from 'antd'
import PropTypes from 'prop-types'
import styles from '../common/index.less'
const FormItem = Form.Item
const formItemLayout = {
  labelCol: {
    xs: { span: 8 },
    sm: { span: 8 },
  },
  wrapperCol: {
    xs: { span: 16 },
    sm: { span: 16 },
  },
}
const queryCol = {
  sm: 8,
  md: 8,
  lg: 6,
  xl: 4,
}
const UpShelfForm = ({
  form,
  dispatch,
  upShelfModal,
  wareTypeData,
}) => {
  let { brandData } = upShelfModal
  const { getFieldDecorator } = form
  const queryFirm = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({ type: 'scoreExchange/updateupShelf', payload: { fields: formValue, tableTime: new Date().getTime() } })
    })
  }
  const reset = () => {
    form.resetFields()
  }
  return (
    <div>
      <Card title="商品配置管理">
        <Row>
          <Col {...queryCol}>
            <FormItem label="商品上架编号" {...formItemLayout}>
              {getFieldDecorator('productNo', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="商品上架名称" {...formItemLayout}>
              {getFieldDecorator('productName', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="商品分类" {...formItemLayout}>
              {getFieldDecorator('typeId', {
              })(
                <TreeSelect
                  style={{ width: '100%' }}
                  treeData={wareTypeData}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="商品品牌" {...formItemLayout}>
              {getFieldDecorator('brandId', {
              })(
                <Select style={{ width: '100%' }}>
                    {brandData && brandData.map(item => <Option key={item.id}>{item.name}</Option>)}
                </Select>
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="积分范围" {...formItemLayout}>
              {getFieldDecorator('scoreGE', {
                rules: [{ required: false, message: '请填数字!', pattern: RegExp('^[0-9]*$') }],
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="——" {...formItemLayout}>
              {getFieldDecorator('scoreLE', {
                rules: [{ required: false, message: '请填数字!', pattern: RegExp('^[0-9]*$') }],
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="上架状态" {...formItemLayout}>
              {getFieldDecorator('productStatus', {
              })(
                <Select style={{ width: '100%' }}>
                  <Option key={0} >下架</Option>
                  <Option key={1} >上架</Option>
                </Select>
              )}
            </FormItem>
          </Col>
        </Row>
        <Row className={styles.buttonPosition}>
          <Button type="primary" onClick={queryFirm.bind(this)}>查询</Button>
          <Button type="primary" onClick={reset.bind(this)}>重置</Button>
        </Row>
      </Card>
    </div>
  )
}
UpShelfForm.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
  brandModal: PropTypes.obj,
  wareTypeData: PropTypes.array,
  upShelfModal: PropTypes.array,
}
const UpShelfQuery = Form.create()(UpShelfForm)
export default UpShelfQuery
