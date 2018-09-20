import React from 'react'
import { Modal, Form, Input, TreeSelect, Select } from 'antd'
import PropTypes from 'prop-types'
import { changeKeyNames } from 'utils'

const FormItem = Form.Item
const Option = Select.Option

const MenuForm = ({
  menuSimpleData,
  menuModal,
  dispatch,
  currentPid,
  form,
  confirmLoading,
  webModuleList,
}) => {
  const { getFieldDecorator } = form
  const { ModalTitle, visible, values = {} } = menuModal
  const {
    name,
    parentId,
    id,
    url,
    iconUrl,
    orderCode,
    ifLeaf,
    requireAuth,
    webModule,
  } = values
  const menuData = changeKeyNames(menuSimpleData, {
    label: 'name',
    pId: 'parentId',
    value: 'id',
  })

  const handleCancel = () => {
    form.resetFields()
    dispatch({
      type: 'menuManagement/handleModal',
      payload: { menuModal: { visible: false } },
    })
  }
  const handleOk = () => {
    form.validateFields((err, formValues) => {
      if (err) {
        return
      }
      dispatch({
        type: 'menuManagement/menuSave',
        payload: { value: formValues, form },
      })
    })
  }
  const webModelOptions = () => {
    return Object.keys(webModuleList).map(key => (
      <Option value={key}>{webModuleList[key]}</Option>
    ))
  }
  return (
    <Modal
      title={ModalTitle}
      visible={visible}
      maskClosable={false}
      confirmLoading={confirmLoading}
      onOk={handleOk}
      onCancel={handleCancel}
    >
      <Form layout="vertical">
        <FormItem label="菜单名称">
          {getFieldDecorator('name', {
            rules: [{ required: true, message: '请填写此项' }],
            initialValue: name,
          })(<Input />)}
        </FormItem>
        <FormItem label="上级菜单">
          {getFieldDecorator('parentId', {
            initialValue: parentId || currentPid,
          })(
            <TreeSelect
              showSearch
              treeDataSimpleMode
              treeNodeFilterProp="name"
              treeData={menuData}
            />
          )}
        </FormItem>
        <FormItem label="菜单链接">
          {getFieldDecorator('url', {
            initialValue: url,
          })(<Input />)}
        </FormItem>
        <FormItem label="菜单图标">
          {getFieldDecorator('iconUrl', {
            initialValue: iconUrl,
          })(<Input />)}
        </FormItem>
        <FormItem label="是否叶子节点">
          {getFieldDecorator('ifLeaf', {
            rules: [{ required: true, message: '请选择此项' }],
            initialValue: ifLeaf,
          })(
            <Select>
              <Option value={1}>是</Option>
              <Option value={0}>否</Option>
            </Select>
          )}
        </FormItem>
        <FormItem label="所属模块">
          {getFieldDecorator('webModule', {
            rules: [{ required: true, message: '请选择此项' }],
            initialValue: webModule,
          })(
            <Select>
              {webModelOptions()}
            </Select>
          )}
        </FormItem>
        <FormItem label="是否认证">
          {getFieldDecorator('requireAuth', {
            rules: [{ required: true, message: '请选择此项' }],
            initialValue: requireAuth,
          })(
            <Select>
              <Option value="YES">是</Option>
              <Option value="NO">否</Option>
            </Select>
          )}
        </FormItem>
        <FormItem label="排序编号">
          {getFieldDecorator('orderCode', {
            rules: [{ required: true, message: '请填写此项' }],
            initialValue: orderCode,
          })(<Input type="number" />)}
        </FormItem>
        <FormItem>
          {getFieldDecorator('id', {
            initialValue: id,
          })(<Input type="hidden" />)}
        </FormItem>
      </Form>
    </Modal>
  )
}

const MenuModal = Form.create()(MenuForm)

MenuForm.propTypes = {
  form: PropTypes.obj,
  ModalTitle: PropTypes.string,
  handleModal: PropTypes.func,
  menuModal: PropTypes.obj,
  menuSimpleData: PropTypes.array,
  currentPid: PropTypes.string,
  queryMenuList: PropTypes.func,
  dispatch: PropTypes.func.isRequired,
  confirmLoading: PropTypes.bool,
  webModuleList: PropTypes.object,
}

export default MenuModal
