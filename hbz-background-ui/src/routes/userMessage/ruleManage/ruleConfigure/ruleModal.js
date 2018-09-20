import React from 'react'
import { Modal, Form, Col, Row, Input, Select } from 'antd'
import PropTypes from 'prop-types'

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
const { TextArea } = Input

const RuleModal = ({
  dispatch,
  visible,
  form,
  ruleDetail,
  adjusts,
  states,
  types,
  hideModal,
  loading,
}) => {
  console.log('loading', loading)
  const { getFieldDecorator } = form
  const handleSubmit = () => {
    form.validateFields((err, formVlaue) => {
      if (err) {
        return
      }
      if (ruleDetail) {
        formVlaue.id = ruleDetail.id
      }
      dispatch({
        type: 'ruleManage/updateRule',
        payload: formVlaue,
      })
    })
  }
  return (
    <div>
      <Modal
        title={ruleDetail ? '编辑规则' : '新增规则'}
        visible={visible}
        onCancel={hideModal}
        onOk={handleSubmit}
        confirmLoading={loading.global}
      >
        <Form>
          {
            ruleDetail ? (
              <Row>
                <Col>
                  <FormItem label="规则编号" {...formItemLayout}>
                    {getFieldDecorator('formulaNo', {
                      initialValue: ruleDetail && ruleDetail.formulaNo,
                    })(
                      <Input disabled />
                    )}
                  </FormItem>
                </Col>
              </Row>
            ) : null
          }
          <Row>
            <Col>
              <FormItem label="规则名称" {...formItemLayout}>
                {getFieldDecorator('name', {
                  rules: [{ required: true, message: '请填写规则名称' }],
                  initialValue: ruleDetail && ruleDetail.name,
                })(
                  <Input placeholder="请填写规则名称" />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col>
              <FormItem label="规则类型" {...formItemLayout}>
                {getFieldDecorator('formulaType', {
                  rules: [{ required: true, message: '请选择规则类型' }],
                  initialValue: ruleDetail && ruleDetail.formulaType,
                })(
                  <Select placeholder="请选择规则类型">
                    {
                      types.map(item => (
                        <Option value={item.val}>{item.name}</Option>
                      ))
                    }
                  </Select>
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col>
              <FormItem label="适应业务" {...formItemLayout}>
                {getFieldDecorator('adjustType', {
                  rules: [{ required: true, message: '请选择适应业务' }],
                  initialValue: ruleDetail && ruleDetail.adjustType,
                })(
                  <Select placeholder="请选择适应业务">
                    {
                      adjusts.map(item => (
                        <Option value={item.val}>{item.name}</Option>
                      ))
                    }
                  </Select>
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col>
              <FormItem label="计算公式" {...formItemLayout}>
                {getFieldDecorator('formula', {
                  rules: [{ required: true, message: '请填写计算公式' }],
                  initialValue: ruleDetail && ruleDetail.formula,
                })(
                  <Input placeholder="请填写计算公式" />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col>
              <FormItem label="公式key" {...formItemLayout}>
                {getFieldDecorator('formulaKey', {
                  rules: [{ required: true, message: '请填写公式key' }],
                  initialValue: ruleDetail && ruleDetail.formulaKey,
                })(
                  <Input placeholder="请填写公式key" />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col>
              <FormItem label="公式解释" {...formItemLayout}>
                {getFieldDecorator('formulaDesc', {
                  rules: [{ required: true, message: '请填写公式解释' }],
                  initialValue: ruleDetail && ruleDetail.formulaDesc,
                })(
                  <TextArea placeholder="请填写公式解释" rows={4} />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col>
              <FormItem label="规则状态" {...formItemLayout}>
                {getFieldDecorator('formulaState', {
                  rules: [{ required: true, message: '请选择规则状态' }],
                  initialValue: ruleDetail && ruleDetail.formulaState,
                })(
                  <Select placeholder="请选择规则状态">
                    {
                      states.map(item => (
                        <Option value={item.val}>{item.name}</Option>
                      ))
                    }
                  </Select>
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col>
              <FormItem label="备注" {...formItemLayout}>
                {getFieldDecorator('comments', {
                  rules: [{ required: false, message: '请填写备注' }],
                  initialValue: ruleDetail && ruleDetail.comments,
                })(
                  <TextArea placeholder="请填写备注" rows={4} />
                )}
              </FormItem>
            </Col>
          </Row>
        </Form>
      </Modal>
    </div>
  )
}

RuleModal.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
  visible: PropTypes.array,
  ruleDetail: PropTypes.array,
  adjusts: PropTypes.array,
  types: PropTypes.array,
  states: PropTypes.array,
  hideModal: PropTypes.func,
  loading: PropTypes.isRequired,
}
const RuleModalForm = Form.create()(RuleModal)
export default RuleModalForm
