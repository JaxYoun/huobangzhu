import React from 'react'
import PropTypes from 'prop-types'
import { Upload, Icon, message } from 'antd'

function getBase64 (img, callback) {
  const reader = new FileReader()
  reader.addEventListener('load', () => callback(reader.result))
  reader.readAsDataURL(img)
}

function beforeUpload (file) {
  const isJPG = file.type === 'image/jpeg'
  if (!isJPG) {
    message.error('只能上传jpg格式图片!')
  }
  const isLt200kb = file.size / 1024 < 200
  if (!isLt200kb) {
    message.error('图片需要小于200kb!')
  }
  return isJPG && isLt200kb
}

class Avatar extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      height: '7vw',
      width: '7vw',
    }
  }
  handleChange = (info) => {
    getBase64(info.file.originFileObj, imageUrl => {
      const names = this.props.name
      const nameForm = this.props.nameForm
      this.props.dispatch({
        type: this.props.type,
        payload: { [names]: imageUrl },
      })

      this.props.form.setFieldsValue({
        [nameForm]: imageUrl,
      })
    })
  }
  onClick = () => {
    let heights = 0
    let widths = 0
    if (this.state.height === '7vw') {
      heights = '15vw'
      widths = '15vw'
    } else {
      heights = '7vw'
      widths = '7vw'
    }
    this.setState({
      height: heights,
      width: widths,
    })
  }
  render () {
    const imageUrl = this.props.imgUrl || ''
    const widths = this.state.width
    const heights = this.state.height
    return (
      <div>
        {
          imageUrl ?
            <img src={imageUrl} alt="点击上传图片" className="avatar" onClick={this.onClick.bind(this)} style={{ width: widths, height: heights, cursor: 'pointer' }} /> :
            <Icon type="plus" className="avatar-uploader-trigger" />
        }
      </div>
    )
  }
}
Avatar.propTypes = {
  uploadImgs: PropTypes.func,
  imgUrl: PropTypes.string,
  dispatch: PropTypes.func,
  type: PropTypes.string,
  name: PropTypes.string,
  nameForm: PropTypes.string,
}
export default Avatar

