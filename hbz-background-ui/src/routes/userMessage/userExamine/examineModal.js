import React from 'react'
import { Modal, Form, Col, Row, Input, Select, DatePicker, Button } from 'antd'
import PropTypes from 'prop-types'
import moment from 'moment'
import Avatar from './upload'
import styles from './index.less'
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

const ExamineModalForm = ({
  visible,
  form,
  dispatch,
  firmData,
  registryData,
  registryProgressData,
  certificateTypeData,
  imgBusinessLicense,
  imgCertifiedPhoto,
  imgId,
  imgVehicle45degreePhoto,
  transTypeData,
  imgDrivingLicense,
  imgDrivingLicense2,
  imgPlyLicense,
  imgPlyLicense2,
  loading,
}) => {
  const { getFieldDecorator } = form
  const selectDataGet = (data) => {
    let arrays = []
    for (let key in data) {
      arrays.push(<Option key={key}>{data[key]}</Option>)
    }
    return arrays
  }
  registryData = selectDataGet(registryData)
  registryProgressData = selectDataGet(registryProgressData)
  certificateTypeData = selectDataGet(certificateTypeData)
  transTypeData = selectDataGet(transTypeData)
  // 关闭模态框
  const hideModal = () => {
    dispatch({ type: 'userExamine/updatefirmModal', payload: { visible: false,
      imgDrivingLicense: '',
      imgDrivingLicense2: '',
      imgPlyLicense: '',
      imgPlyLicense2: '',
      imgBusinessLicense: '',
      imgCertifiedPhoto: '',
      imgId: '',
      imgVehicle45degreePhoto: '',
    } })
    form.resetFields()
  }
  // 提交
  const handleSubmit = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({ type: 'userExamine/updatefirmModal', payload: { loading: true } })
      let dates = new Date(formValue.owerCreateTime).getTime()
      dates = moment(dates).format('YYYY-MM-DD')
      formValue.owerCreateTime = dates
      formValue.id = firmData.id
      formValue.userId = firmData.user.id
      if (firmData.registryCode === 'EnterpriseConsignor') {
        // 企业货主
        dispatch({ type: 'userExamine/addQyhz', payload: formValue })
      } else if (firmData.registryCode === 'TransEnterprise') {
        // 运输企业
        dispatch({ type: 'userExamine/addYsqy', payload: formValue })
      } else if (firmData.registryCode === 'PersonDriver') {
        // 个人司机
        let date = new Date(formValue.drivingValidity).getTime()
        date = moment(date).format('YYYY-MM-DD')
        formValue.drivingValidity = date
        dispatch({ type: 'userExamine/addGrsj', payload: formValue })
      } else if (firmData.registryCode === 'DeliveryBoy') {
        // 运输企业
        dispatch({ type: 'userExamine/addPsy', payload: formValue })
      } else {
        // 个人货主
        dispatch({ type: 'userExamine/addGrhz', payload: formValue })
      }
      form.resetFields()
    })
  }
  // 公共
  const common = () => {
    return (
      <div>
        <Row>
          <Col span={12}>
            <FormItem label="资质类型" {...formItemLayout}>
              {getFieldDecorator('registryCode', {
                initialValue: firmData && firmData.registryCode,
                rules: [{ required: true, message: '请填写资质类型' }],
              })(
                <Select style={{ width: '100%' }} disabled >
                {registryData}
                </Select>
              )}
            </FormItem>
          </Col>
          <Col span={12}>
            <FormItem label="注册进度" {...formItemLayout}>
              {getFieldDecorator('registryProgress', {
                initialValue: firmData && firmData.registryProgress,
                rules: [{ required: true, message: '请填写注册进度' }],
              })(
                <Select style={{ width: '100%' }} disabled >
                {registryProgressData}
                </Select>
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={12}>
            <FormItem label="代表人" {...formItemLayout}>
              {getFieldDecorator('owerName', {
                initialValue: firmData && firmData.owerName,
                rules: [{ required: true, message: '请填写代表人' }],
              })(
                <Input />
              )}
            </FormItem>
          </Col>
          <Col span={12}>
            <FormItem label="出生/公司成立日期" {...formItemLayout}>
              {getFieldDecorator('owerCreateTime', {
                initialValue: firmData.owerCreateTime ? moment(firmData.owerCreateTime) : '',
                rules: [{ required: true, message: '请填写出生日期/公司成立日期' }],
              })(
                <DatePicker />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={12}>
            <FormItem label="证件类型" {...formItemLayout}>
              {getFieldDecorator('certificateType', {
                initialValue: firmData && firmData.certificateType,
                rules: [{ required: true, message: '请填写证件类型' }],
              })(
                <Select style={{ width: '100%' }} disabled >
                  {certificateTypeData}
                </Select>
              )}
            </FormItem>
          </Col>
          <Col span={12}>
            <FormItem label="证件号" {...formItemLayout}>
              {getFieldDecorator('certificateNo', {
                initialValue: firmData && firmData.certificateNo,
                rules: [{ required: true, message: '请填写证件号' }],
              })(
                <Input />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={12}>
            <FormItem label="证件图片" {...formItemLayout}>
              {getFieldDecorator('certificates', {
                initialValue: firmData && firmData.certificates,
                rules: [{ required: true, message: '请填写证件图片' }],
              })(
                <Input disabled style={{ display: 'none' }} />
              )}
              <Avatar
                imgUrl={ imgId || firmData.certificates}
                dispatch={dispatch}
                type="userExamine/updatefirmModal"
                name="imgId"
                nameForm="certificates"
                form={form}
              />
            </FormItem>
          </Col>
        </Row>
      </div>
    )
  }
  // 企业货主 运输企业
  const qyhzAndysqyModal = () => {
    return (
      <div>
        <Row>
          <Col span={12}>
            <FormItem label="组织机构" {...formItemLayout}>
              {getFieldDecorator('organizationName', {
                initialValue: firmData && firmData.organizationName,
                rules: [{ required: true, message: '请填写组织机构' }],
              })(
                <Input />
              )}
            </FormItem>
          </Col>
          <Col span={12}>
            <FormItem label="组织机构代码" {...formItemLayout}>
              {getFieldDecorator('organizationCode', {
                initialValue: firmData && firmData.organizationCode,
                rules: [{ required: true, message: '请填写组织机构代码' }],
              })(
                <Input />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={12}>
            <FormItem label="税号" {...formItemLayout}>
              {getFieldDecorator('dutyParagraph', {
                initialValue: firmData && firmData.dutyParagraph,
                rules: [{ required: true, message: '请填写税号' }],
              })(
                <Input />
              )}
            </FormItem>
          </Col>
          <Col span={12}>
            <FormItem label="帐号" {...formItemLayout}>
              {getFieldDecorator('accountNo', {
                initialValue: firmData && firmData.accountNo,
                rules: [{ required: true, message: '请填写帐号' }],
              })(
                <Input />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={12}>
            <FormItem label="资金" {...formItemLayout}>
              {getFieldDecorator('registryMoney', {
                initialValue: firmData && firmData.registryMoney,
                rules: [{ required: true, message: '请填写资金' }],
              })(
                <Input />
              )}
            </FormItem>
          </Col>
          <Col span={12}>
            <FormItem label="开户银行" {...formItemLayout}>
              {getFieldDecorator('bank', {
                initialValue: firmData && firmData.bank,
                rules: [{ required: true, message: '请填写开户银行' }],
              })(
                <Input />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={12}>
            <FormItem label="营业执照" {...formItemLayout}>
              {getFieldDecorator('businessLicense', {
                initialValue: firmData && firmData.businessLicense,
                rules: [{ required: true, message: '请填写营业执照' }],
              })(
                <Input disabled style={{ display: 'none' }} />
              )}
              <Avatar
                imgUrl={ imgBusinessLicense || firmData.businessLicense}
                dispatch={dispatch}
                type="userExamine/updatefirmModal"
                name="imgBusinessLicense"
                nameForm="businessLicense"
                form={form}
              />
            </FormItem>
          </Col>
        </Row>
      </div>
    )
  }
  // 配送员 货主
  const psyAndhzModal = () => {
    return (
      <div>
        <Row>
          <Col span={12}>
            <FormItem label="持证照" {...formItemLayout}>
              {getFieldDecorator('certifiedPhoto', {
                initialValue: firmData && firmData.certifiedPhoto,
                rules: [{ required: true, message: '请填写持证照' }],
              })(
                <Input disabled style={{ display: 'none' }} />
              )}
              <Avatar
                imgUrl={ imgCertifiedPhoto || firmData.certifiedPhoto}
                dispatch={dispatch}
                type="userExamine/updatefirmModal"
                name="imgCertifiedPhoto"
                nameForm="certifiedPhoto"
                form={form}
              />
            </FormItem>
          </Col>
        </Row>
      </div>
    )
  }
  // 司机
  const grsjModal = () => {
    return (
      <div>
        <Row>
          <Col span={12}>
            <FormItem label="持证照" {...formItemLayout}>
              {getFieldDecorator('certifiedPhoto', {
                initialValue: firmData && firmData.certifiedPhoto,
                rules: [{ required: true, message: '请填写持证照' }],
              })(
                <Input disabled style={{ display: 'none' }} />
              )}
              <Avatar
                imgUrl={ imgCertifiedPhoto || firmData.certifiedPhoto}
                dispatch={dispatch}
                type="userExamine/updatefirmModal"
                name="imgCertifiedPhoto"
                nameForm="certifiedPhoto"
                form={form}
              />
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={12}>
            <FormItem label="到期日期" {...formItemLayout}>
              {getFieldDecorator('drivingValidity', {
                initialValue: firmData.drivingValidity ? moment(firmData.drivingValidity) : '',
                rules: [{ required: true, message: '请填写到期日期' }],
              })(
                <DatePicker />
              )}
            </FormItem>
          </Col>
          <Col span={12}>
            <FormItem label="驾照编号" {...formItemLayout}>
              {getFieldDecorator('licensePlateNumber', {
                initialValue: firmData && firmData.licensePlateNumber,
                rules: [{ required: true, message: '请填写驾照编号' }],
              })(
                <Input />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={12}>
            <FormItem label="45°照" {...formItemLayout}>
              {getFieldDecorator('vehicle45degreePhoto', {
                initialValue: firmData && firmData.vehicle45degreePhoto,
                rules: [{ required: true, message: '请填写45°照' }],
              })(
                <Input disabled style={{ display: 'none' }} />
              )}
              <Avatar
                imgUrl={ imgVehicle45degreePhoto || firmData.vehicle45degreePhoto}
                dispatch={dispatch}
                type="userExamine/updatefirmModal"
                name="imgVehicle45degreePhoto"
                nameForm="vehicle45degreePhoto"
                form={form}
              />
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={12}>
            <FormItem label="车辆类型" {...formItemLayout}>
              {getFieldDecorator('TransType', {
                initialValue: firmData && firmData.TransType,
                rules: [{ required: true, message: '请填写车辆类型' }],
              })(
                <Select style={{ width: '100%' }} >
                {transTypeData}
                </Select>
              )}
            </FormItem>
          </Col>
          <Col span={12}>
            <FormItem label="车辆长度" {...formItemLayout}>
              {getFieldDecorator('transLength', {
                initialValue: firmData && firmData.transLength,
                rules: [{ required: true, message: '请填写车辆长度' }],
              })(
                <Input />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={12}>
            <FormItem label="最大载重" {...formItemLayout}>
              {getFieldDecorator('load', {
                initialValue: firmData && firmData.load,
                rules: [{ required: true, message: '请填写最大载重' }],
              })(
                <Input />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={12}>
            <FormItem label="驾照正面" {...formItemLayout}>
              {getFieldDecorator('drivingLicense', {
                initialValue: firmData && firmData.drivingLicense,
                rules: [{ required: true, message: '请填写驾照正面' }],
              })(
                <Input disabled style={{ display: 'none' }} />
              )}
              <Avatar
                imgUrl={ imgDrivingLicense || firmData.drivingLicense}
                dispatch={dispatch}
                type="userExamine/updatefirmModal"
                name="imgDrivingLicense"
                nameForm="drivingLicense"
                form={form}
              />
            </FormItem>
          </Col>
          <Col span={12}>
            <FormItem label="驾照别一面" {...formItemLayout}>
              {getFieldDecorator('drivingLicense2', {
                initialValue: firmData && firmData.drivingLicense2,
                rules: [{ required: true, message: '请填写驾照别一面' }],
              })(
                <Input disabled style={{ display: 'none' }} />
              )}
              <Avatar
                imgUrl={ imgDrivingLicense2 || firmData.drivingLicense2}
                dispatch={dispatch}
                type="userExamine/updatefirmModal"
                name="imgDrivingLicense2"
                nameForm="drivingLicense2"
                form={form}
              />
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={12}>
            <FormItem label="行驶证第一页照片" {...formItemLayout}>
              {getFieldDecorator('plyLicense', {
                initialValue: firmData && firmData.plyLicense,
                rules: [{ required: true, message: '请填写行驶证第一页照片' }],
              })(
                <Input disabled style={{ display: 'none' }} />
              )}
              <Avatar
                imgUrl={ imgPlyLicense || firmData.plyLicense}
                dispatch={dispatch}
                type="userExamine/updatefirmModal"
                name="imgPlyLicense"
                nameForm="plyLicense"
                form={form}
              />
            </FormItem>
          </Col>
          <Col span={12}>
            <FormItem label="行驶证下一页照片" {...formItemLayout}>
              {getFieldDecorator('plyLicense2', {
                initialValue: firmData && firmData.plyLicense2,
                rules: [{ required: true, message: '请填写行驶证下一页照片' }],
              })(
                <Input disabled style={{ display: 'none' }} />
              )}
              <Avatar
                imgUrl={ imgPlyLicense2 || firmData.plyLicense2}
                dispatch={dispatch}
                type="userExamine/updatefirmModal"
                name="imgPlyLicense2"
                nameForm="plyLicense2"
                form={form}
              />
            </FormItem>
          </Col>
        </Row>
      </div>
    )
  }
  return (
    <div>
      <Modal
        className={styles.addModal}
        title={'客户信息'}
        visible={visible}
        onOk={handleSubmit}
        okText="保存修改"
        cancelText="返回"
        maskClosable={false}
        onCancel={hideModal}
        width={800}
        footer={null}
      >
        <Form>
          {common()}
          {firmData.registryCode === 'EnterpriseConsignor' && qyhzAndysqyModal()}
          {firmData.registryCode === 'TransEnterprise' && qyhzAndysqyModal()}
          {firmData.registryCode === 'PersonDriver' && grsjModal()}
          {firmData.registryCode === 'PersonConsignor' && psyAndhzModal()}
          {firmData.registryCode === 'DeliveryBoy' && psyAndhzModal()}
        </Form>
        <Row className={styles.buttonPosition}>
          <Button type="primary" onClick={handleSubmit.bind(this)} loading={loading}>确定</Button>
          <Button type="primary" onClick={hideModal.bind(this)}>取消</Button>
        </Row>
      </Modal>
    </div>
  )
}
ExamineModalForm.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  firmData: PropTypes.obj,
  registryData: PropTypes.array,
  registryProgressData: PropTypes.array,
  certificateTypeData: PropTypes.array,
  imgBusinessLicense: PropTypes.string,
  imgId: PropTypes.string,
  imgCertifiedPhoto: PropTypes.string,
  imgVehicle45degreePhoto: PropTypes.string,
  transTypeData: PropTypes.array,
  imgDrivingLicense: PropTypes.string,
  imgDrivingLicense2: PropTypes.string,
  imgPlyLicense: PropTypes.string,
  imgPlyLicense2: PropTypes.string,
  loading: PropTypes.bool,
}
const ExamineModal = Form.create()(ExamineModalForm)
export default ExamineModal
