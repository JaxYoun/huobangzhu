import React from 'react'
import PropTypes from 'prop-types'
import { Icon, Card, Form, Row, Col, Input, Button, Upload, message } from 'antd'
import styles from './authentication.less'

const FormItem = Form.Item

const Authentication = ({
  form,
  title,
  action,
  beforeUpload,
  onhandleChange,
  onChange,
  iDcardImage,
  headers,
  licenceImage,
  registryData,
  dispatch,
  now,
}) => {
  const { getFieldDecorator } = form
  const handleSumbit = () => {
    const obj = {
      tabs: now,
    }
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      if (!iDcardImage) {
        message.error('请上传法人身份证照片')
      }
      if (!licenceImage) {
        message.error('请上传营业执照正本照片')
      }
      if (iDcardImage && licenceImage) {
        formValue.certificates = iDcardImage
        formValue.businessLicense = licenceImage
        formValue.certificateType = 'ID'
        obj.value = formValue
        console.log(obj)
        dispatch({
          type: 'personalCenter/submit',
          payload: obj,
        })
      }
    })
  }
  return (
    <div>
      <Card title={title}>
        <Row>
          <Col span={11}>
            <Upload
              className={styles['avatar-uploader']}
              name="image"
              action={action}
              headers={headers}
              beforeUpload={beforeUpload}
              onChange={onChange}
              showUploadList={false}
            >
              {
                iDcardImage ?
                  <img src={`data:image/png;base64,${iDcardImage}`} alt="" className={styles.avatar} /> :
                  <div>
                    <Icon type="plus-circle-o" className={styles['uploader-trigger']} />
                    <p>法人身份证照片</p>
                  </div>
              }
            </Upload>
          </Col>
          <Col span={11} offset={2}>
            <Upload
              className={styles['avatar-uploader']}
              name="image"
              action={action}
              headers={headers}
              beforeUpload={beforeUpload}
              onChange={onhandleChange}
              showUploadList={false}
            >
              {
                licenceImage ?
                  <img src={`data:image/png;base64,${licenceImage}`} alt="" className={styles.avatar} /> :
                  <div>
                    <Icon type="plus-circle-o" className={styles['uploader-trigger']} />
                    <p>营业执照正本</p>
                  </div>
              }
            </Upload>
          </Col>
        </Row>
        <Row style={{ marginTop: '20px' }}>
          <Col span={11}>
            <FormItem label="企业税号">
              {getFieldDecorator('dutyParagraph', {
                initialValue: registryData && registryData.dutyParagraph,
                rules: [
                  {
                    required: true,
                    message: '请输入企业税号！',
                  },
                ],
              })(
                <Input placeholder="请输入企业税号" />
              )}
            </FormItem>
          </Col>
          <Col span={11} offset={2}>
            <FormItem label="开户银行">
              {getFieldDecorator('bank', {
                initialValue: registryData && registryData.bank,
                rules: [
                  {
                    required: true,
                    message: '请输入开户银行！',
                  },
                ],
              })(
                <Input placeholder="请输入开户银行" />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={24}>
            <FormItem label="银行账户">
              {getFieldDecorator('accountNo', {
                initialValue: registryData && registryData.accountNo,
                rules: [
                  {
                    required: true,
                    message: '请输入银行账户！',
                  },
                ],
              })(
                <Input placeholder="请输入银行账户" />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col offset={11}>
            <Button size="large" className={styles.but} onClick={handleSumbit}>提交认证</Button>
          </Col>
        </Row>
      </Card>
    </div>
  )
}

Authentication.propTypes = {
  form: PropTypes.isRequired,
  dispatch: PropTypes.func,
  title: PropTypes.string,
  beforeUpload: PropTypes.func,
  onChange: PropTypes.func,
  action: PropTypes.string,
  iDcardImage: PropTypes.string,
  onhandleChange: PropTypes.func,
  licenceImage: PropTypes.string,
  headers: PropTypes.object,
  registryData: PropTypes.object,
  now: PropTypes.string,
}
const AuthenticationForm = Form.create()(Authentication)
export default AuthenticationForm
