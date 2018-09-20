import React from 'react'
import { Modal, Form, Col, Row, Input, DatePicker, Select, Button } from 'antd'
import PropTypes from 'prop-types'
import Avatar from '../common/uploads'
import styles from '../common/index.less'
import Text from '../../integralShop/common/editor'
import moment from 'moment'
const FormItem = Form.Item
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
const PicAndArtModal = ({
  visible,
  form,
  dispatch,
  firmData,
  imgUrlSmall,
  infoData,
  introductionData,
  loading,
  text,
}) => {
  const { getFieldDecorator } = form
  const handleSubmit = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        dispatch({ type: 'informationManagement/updatePicAndArtModal', payload: { introductionData: '', imgUrlSmall: '', text: true } })
        return
      }
      dispatch({ type: 'informationManagement/updatePicAndArtModal', payload: { loading: true } })
      formValue.content = formValue.introduction
      formValue.publishDate = new Date(formValue.publishDate).getTime()
      if (firmData.id) {
        formValue.status = firmData.status
        formValue.id = firmData.id
        formValue.newsNo = firmData.newsNo
        dispatch({ type: 'informationManagement/updateNews', payload: formValue })
      } else {
        formValue.status = '1'
        dispatch({ type: 'informationManagement/addNews', payload: formValue })
      }
    })
  }
  // 关闭模态框
  const hideModal = () => {
    dispatch({ type: 'informationManagement/cleanModal' })
    form.resetFields()
  }
  // 重置
  const cleanFirm = () => {
    form.resetFields()
    dispatch({ type: 'informationManagement/updatePicAndArtModal', payload: { introductionData: '', imgUrlSmall: '', text: false } })
  }
  return (
    <div>
      <Modal
        title={firmData.id ? '修改图文信息' : '新增图文信息'}
        visible={visible}
        onOk={handleSubmit}
        maskClosable={false}
        onCancel={hideModal}
        width={800}
        className={styles.addModal}
        footer={null}
      >
        <Row style={{ fontSize: '18px', fontWeight: 'bold', textAlign: 'center' }} >
          {firmData.id && <span>信息编号：{firmData.newsNo}</span>}
        </Row>
        <Form>
          <Row>
            <Col span={12}>
              <FormItem label="信息标题" {...formItemLayout}>
                {getFieldDecorator('title', {
                  initialValue: firmData && firmData.title,
                  rules: [{ required: true, message: '请填写信息标题' }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="信息类型" {...formItemLayout}>
                {getFieldDecorator('newsType', {
                  initialValue: firmData && firmData.newsType,
                  rules: [{ required: true, message: '请选择信息类型' }],
                })(
                  <Select style={{ width: '100%' }}>
                  {infoData && infoData.map(item => <Option key={item.val}>{item.name}</Option>)}
                  </Select>
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <FormItem label="简述" {...formItemLayouts}>
                {getFieldDecorator('summary', {
                  initialValue: firmData && firmData.summary,
                  rules: [{ required: true, message: '请填写商品简述' }],
                })(
                  <TextArea rows={1} />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="信息小图" {...formItemLayout}>
              {getFieldDecorator('titleImageList', {
                initialValue: imgUrlSmall || firmData.titleImageList,
                rules: [{ required: true, message: '请选择图片!' }],
              })(
                <Input style={{ display: 'none' }} />
              )}
                <Avatar form={form}
                  imgUrl={imgUrlSmall || firmData.titleImageList}
                  dispatch={dispatch}
                  type="informationManagement/updatePicAndArtModal"
                  name="imgUrlSmall"
                  nameForm="titleImageList"
                />
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="发布时间" {...formItemLayout}>
                {getFieldDecorator('publishDate', {
                  initialValue: (firmData && firmData.publishDate) ? moment(Number(firmData.publishDate)) : moment(new Date(), 'YYYY-MM-DD HH:MM:SS'),
                  rules: [{ required: true, message: '请填写发布时间' }],
                })(
                  <DatePicker
                    showTime
                    format="YYYY-MM-DD HH:mm:ss"
                    disabled
                  />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="创建时间显示" {...formItemLayout}>
                {getFieldDecorator('displayPublishDate', {
                  initialValue: firmData && firmData.displayPublishDate,
                  rules: [{ required: true, message: '请填写是否显示创建时间' }],
                })(
                  <Select style={{ width: '100%' }}>
                    <Option key={0} >否</Option>
                    <Option key={1} >是</Option>
                  </Select>
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <FormItem label="商品详情" {...formItemLayouts}>
                {getFieldDecorator('introduction', {
                  initialValue: introductionData || firmData.content,
                  rules: [{ required: true, message: '请填写商品详情' }],
                })(
                  <TextArea rows={4} style={{ display: 'none' }} />
                )}
                <Text
                  textshow={text}
                  form={form}
                  getHtmlData={introductionData || firmData.content}
                  dispatch={dispatch}
                  types="informationManagement/updatePicAndArtModal"
                />
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <FormItem label="备注" {...formItemLayouts}>
                {getFieldDecorator('recordComment', {
                  initialValue: firmData && firmData.recordComment,
                  rules: [{ required: false, message: '请填写备注' }],
                })(
                  <TextArea rows={2} />
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
PicAndArtModal.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  infoData: PropTypes.arry,
  imgUrlSmall: PropTypes.string,
  firmData: PropTypes.obj,
  introductionData: PropTypes.string,
  loading: PropTypes.bool,
  text: PropTypes.bool,
}
const PicAndArtModalForm = Form.create()(PicAndArtModal)
export default PicAndArtModalForm
