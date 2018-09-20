import React from 'react'
import { Modal, Form, Col, Row, Input, Select, DatePicker } from 'antd'
import PropTypes from 'prop-types'
import { regex } from 'utils'
import Avatar from '../../webManagement/common/upload'
import Text from '../../integralShop/common/editor'
import moment from 'moment'
import styles from './index.less'

const FormItem = Form.Item
const Option = Select.Option
const { TextArea } = Input

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
const formItemLayouts = {
  labelCol: {
    xs: { span: 3 },
    sm: { span: 3 },
  },
  wrapperCol: {
    xs: { span: 21 },
    sm: { span: 21 },
  },
}

const InsideModal = ({
  dispatch,
  visible,
  hideModal,
  loading,
  receiveType,
  msgType,
  platType,
  sendType,
  insideMsgDetail,
  form,
  imgUrlSmall,
  introductionData,
  textShow,
  isPhoneNo,
  isSendTime,
}) => {
  const { getFieldDecorator } = form
  const peopleChange = (value) => {
    if (insideMsgDetail) {
      insideMsgDetail.consumerType.val = '1'
    }
    dispatch({
      type: 'insideMsg/changeStates',
      payload: {
        isPhoneNo: value === '4',
        insideMsgDetail: insideMsgDetail ? { ...insideMsgDetail } : '',
      },
    })
  }
  const sendTypeChange = (value) => {
    if (insideMsgDetail) {
      insideMsgDetail.pushType.val = '1'
    }
    dispatch({
      type: 'insideMsg/changeStates',
      payload: {
        isSendTime: value === '2',
        insideMsgDetail: insideMsgDetail ? { ...insideMsgDetail } : '',
      },
    })
  }
  const hanldsumbit = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      if (insideMsgDetail) {
        formValue.id = insideMsgDetail.id
      }
      if (formValue.sendTime) {
        formValue.sendTime = new Date((formValue.sendTime.format('YYYY-MM-DD HH:mm:ss')).replace(new RegExp('-', 'gm'), '/')).getTime()
      }
      formValue.content = formValue.introduction
      dispatch({
        type: 'insideMsg/saveMsg',
        payload: formValue,
      })
    })
  }
  return (
    <div>
      <Modal
        title={insideMsgDetail ? '编辑消息' : '新增消息'}
        visible={visible}
        onCancel={hideModal}
        confirmLoading={loading.global}
        className={styles.addModal}
        onOk={hanldsumbit}
        width={1000}
      >
        <Form>
          <Row>
          {
            insideMsgDetail ? (
              <Col span={11}>
                <FormItem label="消息编号" {...formItemLayout}>
                  {getFieldDecorator('code', {
                    initialValue: insideMsgDetail && insideMsgDetail.code,
                  })(
                    <Input disabled />
                  )}
                </FormItem>
              </Col>
            ) : null
          }
            <Col span={11} offset={insideMsgDetail ? 1 : 0}>
              <FormItem label="消息标题" {...formItemLayout}>
                {getFieldDecorator('title', {
                  rules: [{ required: true, message: '请填写消息标题' }],
                  initialValue: insideMsgDetail && insideMsgDetail.title,
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={11}>
              <FormItem label="消息类型" {...formItemLayout}>
                {getFieldDecorator('messageType', {
                  rules: [{ required: true, message: '请选择消息类型' }],
                  initialValue: insideMsgDetail && insideMsgDetail.messageType.val,
                })(
                  <Select>
                    {
                      msgType.map(item => (
                        <Option value={item.val}>{item.name}</Option>
                      ))
                    }
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={1}>
              <FormItem label="接收平台" {...formItemLayout}>
                {getFieldDecorator('receivePlatformType', {
                  rules: [{ required: true, message: '请选择接收平台' }],
                  initialValue: insideMsgDetail && insideMsgDetail.receivePlatformType.val,
                })(
                  <Select>
                    {
                      platType.map(item => (
                        <Option value={item.val}>{item.name}</Option>
                      ))
                    }
                  </Select>
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={11}>
              <FormItem label="接收人设置" {...formItemLayout}>
                {getFieldDecorator('consumerType', {
                  rules: [{ required: true, message: '请选择接收人' }],
                  initialValue: insideMsgDetail && insideMsgDetail.consumerType.val,
                })(
                  <Select onChange={peopleChange}>
                    {
                      receiveType.map(item => (
                        <Option value={item.val}>{item.name}</Option>
                      ))
                    }
                  </Select>
                )}
              </FormItem>
            </Col>
            {
              isPhoneNo || (insideMsgDetail && insideMsgDetail.consumerType.val === '4') ? (
                <Col span={11} offset={1}>
                  <FormItem label="接收人手机号码" {...formItemLayout}>
                    {getFieldDecorator('consumerPhoneNo', {
                      rules: [
                        { required: true, message: '请填写接收人号码' },
                        { pattern: regex.phone, message: '请输入正确的电话号码' },
                      ],
                      initialValue: insideMsgDetail && insideMsgDetail.consumerPhoneNo,
                    })(
                      <Input />
                    )}
                  </FormItem>
                </Col>
              ) : null
            }
          </Row>
          <Row>
            <Col span={11}>
              <FormItem label="发送方式" {...formItemLayout}>
                {getFieldDecorator('pushType', {
                  rules: [{ required: true, message: '请选择发送方式' }],
                  initialValue: insideMsgDetail && insideMsgDetail.pushType.val,
                })(
                  <Select onChange={sendTypeChange}>
                    {
                      sendType.map(item => (
                        <Option value={item.val}>{item.name}</Option>
                      ))
                    }
                  </Select>
                )}
              </FormItem>
            </Col>
            {
              isSendTime || (insideMsgDetail && insideMsgDetail.pushType.val === '2') ? (
                <Col span={11} offset={1}>
                  <FormItem label="消息发送时间" {...formItemLayout}>
                    {getFieldDecorator('sendTime', {
                      rules: [{ required: true, message: '请选择消息发送时间' }],
                      initialValue: insideMsgDetail && moment(insideMsgDetail.sendTime),
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
              ) : null
            }
          </Row>
          <Row>
            <Col span={11}>
              <FormItem label="是否已发送" {...formItemLayout}>
                {getFieldDecorator('ifSend', {
                  rules: [{ required: true, message: '请选择是否发送' }],
                  initialValue: insideMsgDetail && insideMsgDetail.ifSend,
                })(
                  <Select>
                    <Option value="0">否</Option>
                    <Option value="1">是</Option>
                  </Select>
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={11}>
              <FormItem label="信息小图" {...formItemLayout}>
                {getFieldDecorator('imagePath', {
                  initialValue: imgUrlSmall || insideMsgDetail.imagePath,
                  rules: [{ required: true, message: '请选择图片!' }],
                })(
                  <Input style={{ display: 'none' }} />
                )}
                <Avatar form={form}
                  imgUrl={imgUrlSmall || insideMsgDetail.imagePath}
                  dispatch={dispatch}
                  type="insideMsg/changeStates"
                  name="imgUrlSmall"
                  nameForm="imagePath"
                />
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <FormItem label="消息详情" {...formItemLayouts}>
                {getFieldDecorator('introduction', {
                  initialValue: introductionData || insideMsgDetail.content,
                  rules: [{ required: true, message: '请填写消息详情' }],
                })(
                  <TextArea rows={4} style={{ display: 'none' }} />
                )}
                <Text textshow={textShow}
                  form={form}
                  getHtmlData={introductionData || insideMsgDetail.content}
                  dispatch={dispatch}
                  types="insideMsg/changeStates"
                />
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <FormItem label="摘要" {...formItemLayouts}>
                {getFieldDecorator('summary', {
                  rules: [{ required: true, message: '请填写备注' }],
                  initialValue: insideMsgDetail && insideMsgDetail.summary,
                })(
                  <TextArea rows={6} />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <FormItem label="备注" {...formItemLayouts}>
                {getFieldDecorator('remark', {
                  rules: [{ required: false, message: '请填写备注' }],
                  initialValue: insideMsgDetail && insideMsgDetail.remark,
                })(
                  <TextArea rows={6} />
                )}
              </FormItem>
            </Col>
          </Row>
        </Form>
      </Modal>
    </div>
  )
}

InsideModal.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
  visible: PropTypes.array,
  insideMsgDetail: PropTypes.object,
  receiveType: PropTypes.array,
  msgType: PropTypes.array,
  platType: PropTypes.array,
  sendType: PropTypes.array,
  hideModal: PropTypes.func,
  loading: PropTypes.isRequired,
  imgUrlSmall: PropTypes.string,
  introductionData: PropTypes.string,
  textShow: PropTypes.bool,
  isPhoneNo: PropTypes.bool,
  isSendTime: PropTypes.bool,
}
const InsideModalForm = Form.create()(InsideModal)
export default InsideModalForm
