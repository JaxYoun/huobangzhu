import React from 'react'
import { Form, Input, Row, Col, Button, Card, Select } from 'antd'
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
const BrandForm = ({
  form,
  dispatch,
  brandModal,
}) => {
  const { provinceData,
    cityData,
    countyData,
  } = brandModal
  const { getFieldDecorator } = form
  const queryFirm = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      if (formValue.countyToId) {
        formValue.areaCode = formValue.countyToId
      } else if (formValue.cityToId) {
        formValue.areaCode = formValue.cityToId
      } else {
        formValue.areaCode = formValue.provinceToId
      }
      dispatch({ type: 'basicData/updateBrand', payload: { fields: formValue, tableTime: new Date().getTime() } })
    })
  }
  const reset = () => {
    form.resetFields()
  }
  const addBrand = () => {
    dispatch({ type: 'basicData/updateModalProvince', payload: { parentId: 0 } })
    dispatch({ type: 'basicData/updateBrandModal', payload: { visible: true } })
  }
  // 取货省市联动查询
  const handleProvinceFromChange = (data, value) => {
    let id = ''
    for (let key in data) {
      if (value === data[key].outCode) {
        id = data[key].id
      }
    }
    // 判断是不是直辖市
    if (id === 1 || id === 790 || id === 2234 || id === 18) {
      dispatch({ type: 'basicData/updateFromCounty', payload: { parentId: id } })
    } else {
      dispatch({ type: 'basicData/updateFromCity', payload: { parentId: id } })
    }
    dispatch({ type: 'basicData/updateBrandModal', payload: { cityData: '', countyData: '' } })
    form.resetFields(['cityToId', 'countyToId'])
  }
  const handleCityFromChange = (data, value) => {
    let id = ''
    for (let key in data) {
      if (value === data[key].outCode) {
        id = data[key].id
      }
    }
    dispatch({ type: 'basicData/updateBrandModal', payload: { countyData: '' } })
    form.resetFields(['countyToId'])
    dispatch({ type: 'basicData/updateFromCounty', payload: { parentId: id } })
  }
  return (
    <div>
      <Card title="商品品牌配置">
        <Row>
          <Col {...queryCol}>
            <FormItem label="品牌编号" {...formItemLayout}>
              {getFieldDecorator('brandNo', {
                rules: [{ required: false, message: '分类编号!' }],
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="品牌名称" {...formItemLayout}>
              {getFieldDecorator('name', {
                rules: [{ required: false, message: '分类编号!' }],
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="订单归属省" {...formItemLayout}>
              {getFieldDecorator('provinceToId', {
              })(
                <Select onChange={handleProvinceFromChange.bind(this, provinceData)} style={{ width: '100%' }}>
                  {provinceData && provinceData.map(item => <Option key={item.outCode}>{item.areaName}</Option>)}
                </Select>
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="归属市" {...formItemLayout}>
              {getFieldDecorator('cityToId', {
              })(
                <Select onChange={handleCityFromChange.bind(this, cityData)} style={{ width: '100%' }}>
                  {cityData && cityData.map(item => <Option key={item.outCode}>{item.areaName}</Option>)}
                </Select>
              )}
            </FormItem>
          </Col>
          <Col {...queryCol} >
            <FormItem label="归属区" {...formItemLayout}>
              {getFieldDecorator('countyToId', {
              })(
                <Select style={{ width: '100%' }}>
                  {countyData && countyData.map(item => <Option key={item.outCode}>{item.areaName}</Option>)}
                </Select>
              )}
            </FormItem>
          </Col>
        </Row>
        <Row className={styles.buttonPosition}>
          <Button type="primary" onClick={queryFirm.bind(this)}>查询</Button>
          <Button type="primary" onClick={reset.bind(this)}>重置</Button>
          <Button type="warning" onClick={addBrand.bind(this)}>新增</Button>
        </Row>
      </Card>
    </div>
  )
}
BrandForm.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
  brandModal: PropTypes.obj,
  provinceData: PropTypes.array,
  cityData: PropTypes.array,
  countyData: PropTypes.array,
}
const BrandQuery = Form.create()(BrandForm)
export default BrandQuery
