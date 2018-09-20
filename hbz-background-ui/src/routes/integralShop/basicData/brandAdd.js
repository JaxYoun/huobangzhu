import React from 'react'
import { Modal, Form, Col, Row, Input, Select, Button } from 'antd'
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
const formItemLayoutselect = {
  labelCol: {
    xs: { span: 10 },
    sm: { span: 10 },
  },
  wrapperCol: {
    xs: { span: 14 },
    sm: { span: 14 },
  },
}
const formItemLayoutselects = {
  labelCol: {
    xs: { span: 12 },
    sm: { span: 12 },
  },
  wrapperCol: {
    xs: { span: 12 },
    sm: { span: 12 },
  },
}
const BrandAddModal = ({
  visible,
  form,
  dispatch,
  firmData,
  imgUrl,
  provinceModalData,
  cityModalData,
  countyModalData,
  loading,
}) => {
  const { getFieldDecorator } = form
  const handleSubmit = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({ type: 'basicData/updateBrandModal', payload: { loading: true } })
      if (formValue.level3AreaCode) {
        formValue.areaCode = formValue.level3AreaCode
      } else if (formValue.level2AreaCode) {
        formValue.areaCode = formValue.level2AreaCode
      } else {
        formValue.areaCode = formValue.level1AreaCode
      }
      if (firmData.id) {
        formValue.id = firmData.id
        dispatch({ type: 'basicData/updateBrands', payload: formValue })
      } else {
        dispatch({ type: 'basicData/addBrand', payload: formValue })
      }
    })
  }
  // 关闭模态框
  const hideModal = () => {
    dispatch({ type: 'basicData/cleanBrandModal' })
    form.resetFields()
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
    if (value === '110000' || value === '120000' || value === '310000' || value === '500000') {
      dispatch({ type: 'basicData/updateModalFromCounty', payload: { parentId: id } })
    } else {
      dispatch({ type: 'basicData/updateModalFromCity', payload: { parentId: id } })
    }
    dispatch({ type: 'basicData/updateBrandModal', payload: { cityModalData: '', countyModalData: '' } })
    if (firmData.id) {
      firmData.area.level2AreaCode = ''
      firmData.area.level3AreaCode = ''
    }
    form.resetFields(['level2AreaCode', 'level3AreaCode'])
  }
  const handleCityFromChange = (data, value) => {
    let id = ''
    for (let key in data) {
      if (value === data[key].outCode) {
        id = data[key].id
      }
    }
    dispatch({ type: 'basicData/updateBrandModal', payload: { countyModalData: '' } })
    form.resetFields(['level3AreaCode'])
    dispatch({ type: 'basicData/updateModalFromCounty', payload: { parentId: id } })
  }

  // 重置
  const cleanFirm = () => {
    form.resetFields()
    dispatch({ type: 'basicData/updateBrandModal', payload: { imgUrl: '', cityModalData: '', countyModalData: '' } })
  }

  return (
    <div>
      <Modal
        title={firmData.id ? '新增品牌' : '修改品牌'}
        visible={visible}
        onOk={handleSubmit}
        maskClosable={false}
        onCancel={hideModal}
        width={700}
        className={styles.addModal}
        footer={null}
      >
        <Form>
          <Row>
            <Col span={24}>
              <FormItem label="品牌名称" {...formItemLayout}>
                {getFieldDecorator('name', {
                  initialValue: firmData && firmData.name,
                  rules: [{ required: true, message: '请填写品牌名称' }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <FormItem label="品牌图标" {...formItemLayout}>
              {getFieldDecorator('headerBit', {
                rules: [{ required: true, message: '请选择图片!' }],
                initialValue: imgUrl || firmData.headerBit,
              })(
                <Input style={{ display: 'none' }} />
              )}
                <Avatar form={form}
                  imgUrl={imgUrl || firmData.headerBit}
                  dispatch={dispatch}
                  type="basicData/updateBrandModal"
                  name="imgUrl"
                  nameForm="headerBit"
                />
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="订单归属省" {...formItemLayoutselects}>
                {getFieldDecorator('level1AreaCode', {
                  initialValue: firmData.area && firmData.area.level1AreaCode,
                  rules: [{ required: true, message: '订单归属省' }],
                })(
                  <Select onChange={handleProvinceFromChange.bind(this, provinceModalData)} style={{ width: '100%' }}>
                    {provinceModalData && provinceModalData.map(item => <Option mid={item.id} key={item.outCode}>{item.areaName}</Option>)}
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col span={6}>
              <FormItem label="归属市" {...formItemLayoutselect}>
                {getFieldDecorator('level2AreaCode', {
                  initialValue: firmData.area && firmData.area.level2AreaCode,
                })(
                  <Select onChange={handleCityFromChange.bind(this, cityModalData)} style={{ width: '100%' }}>
                    {cityModalData && cityModalData.map(item => <Option key={item.outCode}>{item.areaName}</Option>)}
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col span={6} >
              <FormItem label="归属区" {...formItemLayoutselect}>
                {getFieldDecorator('level3AreaCode', {
                  initialValue: firmData.area && firmData.area.level3AreaCode,
                })(
                  <Select style={{ width: '100%' }}>
                    {countyModalData && countyModalData.map(item => <Option key={item.outCode}>{item.areaName}</Option>)}
                  </Select>
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
BrandAddModal.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  firmData: PropTypes.obj,
  imgUrl: PropTypes.string,
  provinceModalData: PropTypes.array,
  cityModalData: PropTypes.array,
  countyModalData: PropTypes.array,
  loading: PropTypes.bool,
}
const BrandAddModalForm = Form.create()(BrandAddModal)
export default BrandAddModalForm
