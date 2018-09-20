import React from 'react'
import { Modal, Form, Col, Row, Input, Card, Button } from 'antd'
import PropTypes from 'prop-types'
import styles from '../index.less'
import { Ttable } from 'components'
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
const GoodsModalForm = ({
  visible,
  form,
  dispatch,
  loading,
  fields,
  tableTime,
  formBig,
}) => {
  const { getFieldDecorator } = form
  const columnsData = [
    {
      title: '货物编号',
      dataIndex: 'commodityNumber',
      width: 80,
    },
    {
      title: '货物名称',
      dataIndex: 'commodityName',
      width: 100,
    },
    {
      title: '规格',
      dataIndex: 'specification',
      width: 80,
    },
    {
      title: '包装单位',
      dataIndex: 'packageUnitValue',
      width: 100,
    },
    {
      title: '体积（立方米）',
      dataIndex: 'volume',
      width: 120,
    },
    {
      title: '重量（kg）',
      dataIndex: 'weight',
      width: 100,
    },
    {
      title: '价格（元）',
      dataIndex: 'price',
      width: 120,
    },
    {
      title: '条码',
      dataIndex: 'barcode',
      width: 120,
    },
    {
      title: '货物简拼',
      dataIndex: 'jianpin',
      width: 120,
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/manager/commodityInformationTable',
    fields: fields || {},
    tableTime,
  }
  // 数据查询
  const queryFirm = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({ type: 'receiptManagement/updategoodsModal', payload: { fields: formValue, tableTime: new Date().getTime() } })
    })
  }
  // 重置
  const reset = () => {
    form.resetFields()
  }
  // 关闭模态框
  const hideModal = () => {
    dispatch({ type: 'receiptManagement/cleangoodsModal' })
    form.resetFields()
  }
  // 选择按钮
  const onChange = (selectedRowKeys, selectedRows) => {
    dispatch({ type: 'receiptManagement/update', payload: { goodsMessage: selectedRows[0] } })
    formBig.setFieldsValue({ commodityNumber: selectedRows[0] && selectedRows[0].commodityNumber,
      commodityName: selectedRows[0] && selectedRows[0].commodityName,
      packageUnit: selectedRows[0] && selectedRows[0].packageUnit,
      billingWeight: selectedRows[0] && selectedRows[0].billingWeight,
      volume: selectedRows[0] && selectedRows[0].volume,
      price: selectedRows[0] && selectedRows[0].price,
    })
  }
  let rowSelections = {
    type: 'radio',
    onChange,
  }
  return (
    <div>
      <Modal
        title={'货物信息'}
        visible={visible}
        onOk={hideModal}
        maskClosable={false}
        onCancel={hideModal}
        width={1300}
        footer={null}
        closable={false}
      >
        <Card title="货物信息">
          <Row>
            <Col {...queryCol}>
              <FormItem label="货物编号" {...formItemLayout}>
                {getFieldDecorator('commodityNumber', {
                })(
                  <Input
                    onPressEnter={queryFirm.bind(this)}
                  />
                )}
              </FormItem>
            </Col>
            <Col {...queryCol}>
              <FormItem label="货物名称" {...formItemLayout}>
                {getFieldDecorator('commodityName', {
                })(
                  <Input
                    onPressEnter={queryFirm.bind(this)}
                  />
                )}
              </FormItem>
            </Col>
            <Col {...queryCol}>
              <FormItem label="简拼" {...formItemLayout}>
                {getFieldDecorator('jianpin', {
                })(
                  <Input
                    onPressEnter={queryFirm.bind(this)}
                  />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row className={styles.buttonPosition}>
            <Button type="primary" onClick={queryFirm.bind(this)}>查询</Button>
            <Button type="primary" onClick={reset.bind(this)}>重置</Button>
          </Row>
        </Card>
        <Card>
          <Ttable {...tableProps}
            rowSelection={rowSelections}
            scroll={{ x: 1800 }}
          />
        </Card>
        <Row className={styles.buttonPosition}>
          <Button type="primary" onClick={hideModal.bind(this)} loading={loading}>确定选择</Button>
        </Row>
      </Modal>
    </div>
  )
}
GoodsModalForm.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  firmData: PropTypes.obj,
  getpackageData: PropTypes.array,
  loading: PropTypes.bool,
  fields: PropTypes.array,
  tableTime: PropTypes.string,
  formBig: PropTypes.obj,
}
const GoodsModal = Form.create()(GoodsModalForm)
export default GoodsModal
