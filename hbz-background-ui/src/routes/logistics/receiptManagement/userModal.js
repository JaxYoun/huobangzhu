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
const UserModalForm = ({
  visible,
  form,
  dispatch,
  loading,
  fields,
  tableTime,
  type,
  formBig,
}) => {
  const { getFieldDecorator } = form
  const columnsData = [
    {
      title: '单位名称',
      dataIndex: 'companyName',
      width: 100,
    },
    {
      title: '客户分类',
      dataIndex: 'userClassificationValue',
      width: 80,
    },
    {
      title: '客户名称',
      dataIndex: 'userName',
      width: 100,
    },
    {
      title: '联系电话',
      dataIndex: 'userTelephone',
      width: 120,
    },
    {
      title: '身份证号',
      dataIndex: 'idCard',
      width: 100,
    },
    {
      title: '地址',
      dataIndex: 'userAddress',
      width: 120,
    },
    {
      title: '开户行',
      dataIndex: 'bank',
      width: 120,
    },
    {
      title: '银行账号',
      dataIndex: 'bankAccount',
      width: 120,
    },
    {
      title: '简拼',
      dataIndex: 'jianpin',
      width: 120,
    },
    {
      title: '备注',
      dataIndex: 'remarks',
      width: 200,
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/manager/userInformationTable',
    fields: fields || {},
    tableTime,
  }
  // 数据查询
  const queryFirm = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({ type: 'receiptManagement/updateuserModal', payload: { fields: formValue, tableTime: new Date().getTime() } })
    })
  }
  // 重置
  const reset = () => {
    form.resetFields()
  }
  // 关闭模态框
  const hideModal = () => {
    dispatch({ type: 'receiptManagement/cleanuserModal' })
    form.resetFields()
  }
  // 选择按钮
  const onChange = (selectedRowKeys, selectedRows) => {
    if (type === 'get') {
      dispatch({ type: 'receiptManagement/update', payload: { getuserMessage: selectedRows[0] } })
      formBig.setFieldsValue({ shipperUserCompanyName: selectedRows[0] && selectedRows[0].companyName,
        shipperUserName: selectedRows[0] && selectedRows[0].userName,
        shipperUserTelephone: selectedRows[0] && selectedRows[0].userTelephone,
        shipperUserZipCode: selectedRows[0] && selectedRows[0].shipperUserZipCode,
        shipperUserAddress: selectedRows[0] && selectedRows[0].userAddress,
      })
    } else if (type === 'send') {
      dispatch({ type: 'receiptManagement/update', payload: { senduserMessage: selectedRows[0] } })
      formBig.setFieldsValue({ receiverUserCompanyName: selectedRows[0] && selectedRows[0].companyName,
        receiverUserName: selectedRows[0] && selectedRows[0].userName,
        receiverUserTelephone: selectedRows[0] && selectedRows[0].userTelephone,
        receiverUserZipCode: selectedRows[0] && selectedRows[0].receiverUserZipCode,
        receiverUserAddress: selectedRows[0] && selectedRows[0].userAddress,
      })
    }
  }
  let rowSelections = {
    type: 'radio',
    onChange,
  }
  return (
    <div>
      <Modal
        title={'用户信息'}
        visible={visible}
        onOk={hideModal}
        maskClosable={false}
        onCancel={hideModal}
        width={1300}
        footer={null}
        closable={false}
      >
        <Card title="用户信息">
          <Row>
            <Col {...queryCol}>
              <FormItem label="单位名称" {...formItemLayout}>
                {getFieldDecorator('companyName', {
                })(
                  <Input
                    onPressEnter={queryFirm.bind(this)}
                  />
                )}
              </FormItem>
            </Col>
            <Col {...queryCol}>
              <FormItem label="客户手机号码" {...formItemLayout}>
                {getFieldDecorator('userTelephone', {
                })(
                  <Input
                    onPressEnter={queryFirm.bind(this)}
                  />
                )}
              </FormItem>
            </Col>
            <Col {...queryCol}>
              <FormItem label="客户地址" {...formItemLayout}>
                {getFieldDecorator('userAddress', {
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
UserModalForm.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  firmData: PropTypes.obj,
  getpackageData: PropTypes.array,
  loading: PropTypes.bool,
  fields: PropTypes.array,
  tableTime: PropTypes.string,
  type: PropTypes.string,
  formBig: PropTypes.obj,
}
const UserModal = Form.create()(UserModalForm)
export default UserModal
