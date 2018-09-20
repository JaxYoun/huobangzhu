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
}) => {
  const { getFieldDecorator } = form
  const { ModalTitle, visible, values = {}, confirmLoading } = menuModal
  const { menuName, pId, id, menuUrl, menuIcon, orderCode, menuType = '1', menuCode } = values
  const menuData = changeKeyNames(menuSimpleData, { value: 'id', key: 'id', label: 'menuName' })

  const handleCancel = () => {
    form.resetFields()
    dispatch({ type: 'menu/handleModal', payload: { menuModal: { visible: false } } })
  }
  const handleOk = () => {
    form.validateFields((err, formValues) => {
      if (err) {
        return
      }
      formValues.menuType = Number(formValues.menuType)
      dispatch({ type: 'menu/menuSave', payload: { value: formValues, form } })
    })
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
          {getFieldDecorator('menuName', {
            rules: [{ required: true, message: '请填写此项' }],
            initialValue: menuName,
          })(
            <Input />
          )}
        </FormItem>
        <FormItem label="上级菜单">
          {getFieldDecorator('pId', {
            initialValue: pId || currentPid,
          })(
            <TreeSelect
              showSearch
              treeDataSimpleMode
              treeNodeFilterProp="menuName"
              treeData={menuData}
            />
          )}
        </FormItem>
        <FormItem label="菜单链接">
          {getFieldDecorator('menuUrl', {
            initialValue: menuUrl,
          })(
            <Input />
          )}
        </FormItem>
        <FormItem label="菜单图标">
          {getFieldDecorator('menuIcon', {
            initialValue: menuIcon,
          })(
            <Input />
          )}
        </FormItem>
        <FormItem label="菜单编码">
          {getFieldDecorator('menuCode', {
            initialValue: menuCode,
          })(
            <Input />
          )}
        </FormItem>
        <FormItem label="是否显示">
        {getFieldDecorator('menuType', {
          rules: [{ required: true, message: '请填写此项' }],
          initialValue: `${menuType}`,
        })(
          <Select>
            <Option value="1">是</Option>
            <Option value="0">否</Option>
          </Select>
        )}
        </FormItem>
        <FormItem label="排序编号">
          {getFieldDecorator('orderCode', {
            rules: [{ required: true, message: '请填写此项' }],
            initialValue: orderCode,
          })(
            <Input type="number" />
          )}
        </FormItem>
        <FormItem>
          {getFieldDecorator('id', {
            initialValue: id,
          })(
            <Input type="hidden" />
          )}
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
}

export default MenuModal
