import React from 'react'
import PropTypes from 'prop-types'
import { Form, Col, Input, Modal, Select, DatePicker, Radio, message } from 'antd'
import { Address } from 'components'
import { regex } from 'utils'
import moment from 'moment'
// import styles from '../../../themes/index.less'

const FormItem = Form.Item
const { TextArea } = Input
const RadioButton = Radio.Button
const RadioGroup = Radio.Group

const MsgModal = ({
  visible,
  dispatch,
  modalType,
  formData,
  form,
  commodityType,
  massList,
  countOrderPrice,
  readyToPay,
}) => {
  const commodityTypes = []
  for (let key of Object.keys(commodityType)) {
    commodityTypes.push(<Option value={key}>{commodityType[key]}</Option>)
  }
  const modalTitle = {
    fahuo: '新增发货地址',
    shouhuo: '新增收货地址',
    huowu: '货物详情',
    editFahuo: '编辑发货地址',
    editShouhuo: '编辑收货地址',
  }
  // const changeAddress = (e) => {
  //   console.log(e.target.value)
  //   dispatch({ type: 'accessories/queryLocation', payload: { add: e.target.value } })
  // }
 // 关闭模态框
  const handleCancel = () => {
    dispatch({ type: 'accessories/hideModal', payload: { formData: '', visible: false } })
  }
  // 提交模态框
  const handleSubmit = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      if (modalType === 'huowu') {
        console.log(formValue)
        formValue.orderTakeTime = formValue.orderTakeTime.format('YYYY-MM-DD HH:mm:ss')
        countOrderPrice(formValue)// 计算价格
      } else {
        if (formData.linker) {
          let { id, address, type, linker, linkTelephone, lng, lat, location, usein, idefault } = formData
          let obj = {
            id,
            idefault,
            address,
            type,
            linker,
            linkTelephone,
            lng,
            lat,
            location,
            usein,
            // areaCode: formData.area.outCode,
            ...formValue,
          }
          dispatch({ type: 'accessories/updateAddress', payload: obj })
        } else {
          console.log(formValue)
          formValue.orderType = 'EX'
          formValue.usein = modalType === 'fahuo' ? 1 : 2
          dispatch({ type: 'accessories/addAddress', payload: formValue })
        }
      }
    })
  }
  const { getFieldDecorator } = form
  const addressProps = {
    address: {
      key: 'location',
      value: formData && formData.location,
    },
    label: '联系地址',
    lng: {
      key: 'lng',
      value: formData && formData.lng,
    },
    lat: {
      key: 'lat',
      value: formData && formData.lat,
    },
    form,
  }
  return (
    <Modal
      title={modalTitle[modalType]}
      visible={visible}
      width={'400'}
      wrapClassName="vertical-center-modal"
      onCancel={handleCancel.bind(this)}
      onOk={handleSubmit}
    >
      <div>
        <Form>
          {
            modalType === 'huowu' ? (
              <div>
                <Col span="24">
                  <FormItem label="货物类型" >
                    {getFieldDecorator('commodityClass', {
                      rules: [
                        {
                          required: true,
                          message: '请选择货物类型',
                        },
                      ],
                      initialValue: readyToPay && readyToPay.commodityClass,
                    })(
                      <Select>
                        {commodityTypes}
                      </Select>
                    )}
                  </FormItem>
                </Col>
                <Col span="24">
                  <FormItem label="货物重量" >
                    {getFieldDecorator('commodityWeight', {
                      rules: [
                        {
                          required: true,
                          message: '请选择货物重量',
                        },
                      ],
                      initialValue: readyToPay && readyToPay.commodityWeight,
                    })(
                      <Select>
                        {
                          massList.map(item => (
                            <Option value={item.val}>{item.name}</Option>
                          ))
                        }
                      </Select>
                    )}
                  </FormItem>
                </Col>
                <Col span="24">
                  <FormItem label="预估体积" >
                    {getFieldDecorator('commodityVolume', {
                      rules: [
                        {
                          required: true,
                          message: '请填写预估体积',
                        },
                        { pattern: regex.number, message: '请输入正确的体积!' },
                      ],
                      initialValue: readyToPay && readyToPay.commodityVolume,
                    })(
                      <Input placeholder="请输入预估体积" addonAfter={<span>m³</span>} />
                    )}
                  </FormItem>
                </Col>
                <Col span="24">
                  <FormItem label="取件时间" >
                    {getFieldDecorator('orderTakeTime', {
                      rules: [
                        {
                          required: true,
                          message: '请填写取件时间',
                        },
                      ],
                      initialValue: readyToPay && moment(readyToPay.orderTakeTime, 'YYYY-MM-DD HH:mm:ss'),
                    })(
                      <DatePicker
                        showTime
                        format="YYYY-MM-DD HH:mm:ss"
                        placeholder="取件时间"
                        width={'100%'}
                      />
                    )}
                  </FormItem>
                </Col>
                <Col span="24">
                  <FormItem label="货物描述" >
                    {getFieldDecorator('commodityDesc', {
                      rules: [
                        {
                          required: true,
                          message: '请填写货物描述',
                        },
                      ],
                      initialValue: readyToPay && readyToPay.commodityDesc,
                    })(
                      <TextArea placeholder="请输入货物描述" />
                    )}
                  </FormItem>
                </Col>
              </div>
            ) : (
              <div>
                <Col span="24">
                  <FormItem label="联系人">
                    {getFieldDecorator('linker', {
                      rules: [
                        {
                          required: true,
                          message: '请填写联系人',
                        },
                      ],
                      initialValue: formData && formData.linker,
                    })(
                      <Input placeholder="请输入联系人" />
                    )
                    }
                  </FormItem>
                </Col>
                <Col span="24">
                  {/* <FormItem label="联系地址(例如：四川省成都市金牛区******)">
                    {getFieldDecorator('location', {
                      rules: [
                        {
                          required: true,
                          message: '请填写联系地址',
                        },
                      ],
                      initialValue: formData && formData.location,
                    })(
                      // <Input placeholder="请输入联系地址" onBlur={changeAddress} />
                      <Address />
                    )}
                  </FormItem> */}
                  <Address {...addressProps} placeholder="请输入联系地址" />
                </Col>
                <Col span="24">
                  <FormItem label="联系电话" >
                    {getFieldDecorator('linkTelephone', {
                      rules: [
                        {
                          required: true,
                          message: '请填写取货电话',
                        },
                        { pattern: regex.phone, message: '请输入正确的手机号码!' },
                      ],
                      initialValue: formData && formData.linkTelephone,
                    })(
                      <Input placeholder="请输入联系电话" />
                    )}
                  </FormItem>
                </Col>
                <Col span="24">
                  <FormItem label="门牌号">
                    {getFieldDecorator('address', {
                      rules: [
                        {
                          required: true,
                          message: '请填写门牌号',
                        },
                      ],
                      initialValue: formData && formData.address,
                    })(
                      <Input placeholder="请输入门牌号" />
                    )}
                  </FormItem>
                </Col>
                <Col span="24">
                  <FormItem label="地址标签" >
                    {getFieldDecorator('type', {
                      rules: [
                        {
                          required: true,
                          message: '请选择地址标签',
                        },
                      ],
                      initialValue: formData && formData.type,
                    })(
                      <RadioGroup>
                        <RadioButton value="1">未知</RadioButton>
                        <RadioButton value="2">家</RadioButton>
                        <RadioButton value="3">公司</RadioButton>
                        <RadioButton value="4">常用地</RadioButton>
                      </RadioGroup>
                    )}
                  </FormItem>
                </Col>
              </div>
            )
          }
        </Form>
      </div>
    </Modal>
  )
}

const Modals = Form.create()(MsgModal)
MsgModal.propTypes = {
  form: PropTypes.isRequired,
  msgModal: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
  visible: PropTypes.bool.isRequired,
  modalType: PropTypes.string,
  formData: PropTypes.object,
  addlng: PropTypes.object,
  addlat: PropTypes.object,
  commodityType: PropTypes.object,
  massList: PropTypes.array,
  countOrderPrice: PropTypes.func,
  readyToPay: PropTypes.object,
}
export default Modals

