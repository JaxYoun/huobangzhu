import React from 'react'
import { Form, Input, Row, Col, Button, Card, TreeSelect, Select } from 'antd'
import PropTypes from 'prop-types'
import styles from './index.less'

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
const GoodsForm = ({
  form,
  dispatch,
  wareTypeData,
  goodsModal,
}) => {
  let { brandData } = goodsModal
  const { getFieldDecorator } = form
  const queryFirm = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({ type: 'basicData/updateGoods', payload: { fields: formValue, tableTime: new Date().getTime() } })
    })
  }
  const reset = () => {
    form.resetFields()
  }
  // 编辑新增商品
  const addGoods = () => {
    // 获取树
    dispatch({ type: 'basicData/getTreeData', payload: { } })
    dispatch({ type: 'basicData/updateGoodsModal', payload: { visible: true } })
  }
  return (
    <div>
      <Card title="商品配置管理">
        <Row>
          <Col {...queryCol}>
            <FormItem label="商品编号" {...formItemLayout}>
              {getFieldDecorator('wareNo', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="商品名称" {...formItemLayout}>
              {getFieldDecorator('name', {
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
          <Col{...queryCol}>
            <FormItem label="分类级别" {...formItemLayout}>
              {getFieldDecorator('state', {
              })(
                <Select style={{ width: '100%' }}>
                  <Option key={0} >停用</Option>
                  <Option key={1} >可用</Option>
                </Select>
              )}
            </FormItem>
          </Col>
        </Row>
        <Row className={styles.buttonPosition}>
          <Button type="primary" onClick={queryFirm.bind(this)}>查询</Button>
          <Button type="primary" onClick={reset.bind(this)}>重置</Button>
          <Button type="warning" onClick={addGoods.bind(this)}>新增</Button>
        </Row>
      </Card>
    </div>
  )
}
GoodsForm.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
  brandModal: PropTypes.obj,
  wareTypeData: PropTypes.array,
  goodsModal: PropTypes.array,
}
const GoodsQuery = Form.create()(GoodsForm)
export default GoodsQuery
