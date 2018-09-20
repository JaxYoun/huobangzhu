import React from 'react'
import PropTypes from 'prop-types'
import { TreeSelect, Form, Row, Col, Input, Select, Modal } from 'antd'
import { regex, arrayToTree, changeKeyNames } from 'utils'

const FormItem = Form.Item
const Option = Select.Option
const { TextArea } = Input

const AccountDetail = ({
  dispatch,
  loading,
  accountDetail,
  availableRole,
  visible,
  hideModal,
  orgData,
  form,
}) => {
  const { getFieldDecorator } = form
  const changeOrgName = changeKeyNames(orgData, { value: 'id', key: 'id', pId: 'parentId', label: 'organizationName' })
  const orgArray = arrayToTree(changeOrgName, 'id', 'parentId', 'children')
  const submit = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      if (accountDetail) {
        formValue.id = accountDetail.id
      }
      dispatch({
        type: 'accountManage/submit',
        payload: formValue,
      })
    })
  }
  return (
    <Modal
      visible={visible}
      onCancel={hideModal}
      confirmLoading={loading.global}
      width={800}
      onOk={submit}
      title={accountDetail ? '编辑用户' : '新增用户'}
    >
      <Form>
        <Row>
          <Col span={11}>
            <FormItem label="登录名">
                {getFieldDecorator('login', {
                  rules: [
                    {
                      required: true,
                      message: '请输入登录名',
                    },
                  ],
                  initialValue: accountDetail && accountDetail.login,
                })(
                  <Input placeholder="请输入账户的登录名" />
                )}
            </FormItem>
          </Col>
          <Col span={11} offset={1}>
            <FormItem label="昵称">
                {getFieldDecorator('nickName', {
                  rules: [
                    {
                      required: true,
                      message: '请输入昵称',
                    },
                  ],
                  initialValue: accountDetail && accountDetail.nickName,
                })(
                  <Input placeholder="请输入账户的昵称" />
                )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={11}>
            <FormItem label="手机号">
                {getFieldDecorator('telephone', {
                  rules: [
                    {
                      required: true,
                      message: '请输入手机号码',
                    },
                    { pattern: regex.phone, message: '请填写正确的手机号码' },
                  ],
                  initialValue: accountDetail && accountDetail.telephone,
                })(
                  <Input placeholder="请输入账户的电话号码" />
                )}
            </FormItem>
          </Col>
          <Col span={11} offset={1}>
            <FormItem label="电子邮件">
                {getFieldDecorator('email', {
                  rules: [
                    {
                      required: true,
                      message: '请输入电子邮件',
                    },
                    { pattern: regex.email, message: '请填写正确的电子邮件' },
                  ],
                  initialValue: accountDetail && accountDetail.email,
                })(
                  <Input placeholder="请输入账户的电子邮件" />
                )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={11}>
            <FormItem label="所属部门">
                {getFieldDecorator('orgId', {
                  rules: [
                    {
                      required: true,
                      message: '请选择所属部门',
                    },
                  ],
                  initialValue: accountDetail && accountDetail.orgId,
                })(
                  <TreeSelect
                    treeData={orgArray}
                    placeholder="请选择所属部门"
                    treeDefaultExpandAll
                  />
                )}
            </FormItem>
          </Col>
          <Col span={11} offset={1}>
            <FormItem label="所属角色">
                {getFieldDecorator('roleIds', {
                  rules: [
                    {
                      required: true,
                      message: '请选择所属角色',
                    },
                  ],
                  initialValue: accountDetail ? accountDetail.roleIds : [],
                })(
                  <Select placeholder="请选择所属角色" mode="multiple">
                    {
                      availableRole.map(item => (
                        <Option value={item.id}>{item.roleName}</Option>
                      ))
                    }
                  </Select>
                )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={23}>
            <FormItem label="备注">
                {getFieldDecorator('comments', {
                  initialValue: accountDetail && accountDetail.comments,
                })(
                  <TextArea rows={4} />
                )}
            </FormItem>
          </Col>
        </Row>
      </Form>
    </Modal>
  )
}
AccountDetail.propTypes = {
  form: PropTypes.isRequired,
  accountManage: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
  loading: PropTypes.isRequired,
  accountDetail: PropTypes.object,
  availableRole: PropTypes.array,
  orgData: PropTypes.array,
  visible: PropTypes.bool,
  hideModal: PropTypes.func,
}
const AccountDetailForm = Form.create()(AccountDetail)
export default AccountDetailForm

