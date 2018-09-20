import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { browserHistory } from 'react-router'
import { Icon, Card, Form, Row, Col, Input, Button, Select, TreeSelect, InputNumber } from 'antd'
import { Iconfont, AreaSelect } from 'components'
import { changeKeyNames, regex } from 'utils'
import CarLengthModal from './carLengthModal'
import styles from './index.less'

const FormItem = Form.Item
const Option = Select.Option

const PublishDriverLine = ({
  form,
  dispatch,
  publishDriverLine,
}) => {
  const {
    transTypes,
    visible,
    transSizeData,
    selectCarsId,
    selectCarsName,
  } = publishDriverLine

  // const provinceTreeData = changeKeyNames(provinceData, { value: 'id', key: 'id', pId: 'parentId', label: 'areaName' })
  const { getFieldDecorator } = form
  const numberProps = {
    max: 99999999,
    min: 0.00,
    precision: 2,
  }

  const transTypeObj = []
  for (let key of Object.keys(transTypes)) {
    transTypeObj.push(<Option value={key}>{transTypes[key]}</Option>)
  }

  const showModal = () => {
    dispatch({
      type: 'publishDriverLine/showModals',
    })
    form.resetFields(['transSizes'])
  }
  const hideModal = () => {
    dispatch({
      type: 'publishDriverLine/changeStates',
      payload: {
        visible: false,
        selectCarsId: [],
        selectCarsName: [],
      },
    })
    // if (selectCarsName.length === 0) {
    //   form.resetFields(['transSizes'])
    // }
  }
  const ModalProps = {
    visible,
    transSizeData,
    dispatch,
    hideModal,
    selectCarsId,
    selectCarsName,
  }
  const publish = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      let { transType, originAreaCode, destAreaCode, maxLoad, unitPrices } = formValue
      let obj = {
        transType,
        originAreaCode,
        destAreaCode,
        maxLoad,
        unitPrices,
        transSizes: selectCarsName,
      }
      dispatch({
        type: 'publishDriverLine/createDriverLine',
        payload: obj,
      })
    })
  }
  const getInfo = {
    province: {
      label: '起点站',
      key: 'quProvince',
      outKey: 'originAreaCode',
      required: true,
    },
    form,
  }
  const shouInfo = {
    province: {
      label: '终点站',
      key: 'shouProvince',
      outKey: 'destAreaCode',
      required: true,
    },
    form,
  }
  return (
    <div>
      <Form>
        <Card title="收/发站">
          <Row>
            <Col span={11}>
              <AreaSelect {...getInfo} placeholder="请选择起点站" />
            </Col>
            <Col span={11} offset={2}>
              <AreaSelect {...shouInfo} placeholder="请选择终点站" />
            </Col>
          </Row>
        </Card>
        <Card title="车辆配置">
          <Row>
            <Col span={11}>
              <FormItem label="车辆类型">
                {getFieldDecorator('transType', {
                  rules: [
                    {
                      required: true,
                      message: '请选择收货地址',
                    },
                  ],
                })(
                  <Select>
                    {transTypeObj}
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={2}>
              <FormItem label="最大载重">
                {getFieldDecorator('maxLoad', {
                  rules: [
                    {
                      required: true,
                      message: '请填写最大载重',
                    },
                    { pattern: regex.number, message: '请输入整数或保留1到2位小数的数字' },
                  ],
                })(
                  <Input
                    {...numberProps}
                    placeholder="请输入最大载重"
                    addonAfter={<span>吨</span>}
                  />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={11}>
              <FormItem label="车长选择">
                {getFieldDecorator('transSizes', {
                  rules: [
                    {
                      required: true,
                      message: '请选择收货地址',
                    },
                  ],
                  initialValue: selectCarsName.length > 0 ? selectCarsName.join(',') : '',
                })(
                  <Input onClick={showModal} placeholder="点击选择车长" />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Card title="费用">
          <Row>
            <Col span={11}>
              <FormItem label="单价">
                {getFieldDecorator('unitPrices', {
                  rules: [
                    {
                      required: true,
                      message: '请填写单价',
                    },
                  ],
                })(
                  <Input addonAfter={<span>元/KG</span>} />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
      </Form>
      <Card>
        <Row className={styles.publish}>
          <Col>
            <Button type="primary" onClick={publish}>确 认 发 布</Button>
          </Col>
        </Row>
      </Card>
      {
        visible ? <CarLengthModal {...ModalProps} /> : null
      }
    </div>
  )
}

PublishDriverLine.propTypes = {
  form: PropTypes.isRequired,
  publishDriverLine: PropTypes.isRequired,
  app: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
}
const PublishDriverLineForm = Form.create()(PublishDriverLine)
export default connect(({ publishDriverLine }) => ({ publishDriverLine }))(PublishDriverLineForm)
