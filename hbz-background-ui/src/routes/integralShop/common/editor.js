import React from 'react'
import ReactQuill from 'react-quill'
import PropTypes from 'prop-types'
import 'react-quill/dist/quill.snow.css'

class MyComponent extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      text: this.props.getHtmlData,
      modules: {
        toolbar: [
          [{ header: [1, 2, 3, 4, 5, false] }],
          ['bold', 'italic', 'underline', 'strike', 'blockquote'],
          [{ list: 'ordered' }, { list: 'bullet' }, { indent: '-1' }, { indent: '+1' }],
          ['link', 'image'],
          ['clean'],
        ],
      } }
    this.handleChange = this.handleChange.bind(this)
  }

  handleChange (value) {
    this.setState({ text: value })
    // this.props.dispatch({ type: this.props.types, payload: { introductionData: `${value}` } })
    this.props.form.setFieldsValue({
      introduction: value,
    })
  }

  render () {
    if (!this.props.textshow) {
      this.setState({ text: '' })
      this.props.dispatch({ type: this.props.types, payload: { text: true } })
    }
    let texts = this.state.text || ''
    return (
      <ReactQuill value={texts}
        onChange={this.handleChange}
        modules={this.state.modules}
      />
    )
  }
}
MyComponent.propTypes = {
  dispatch: PropTypes.func,
  types: PropTypes.string,
  getHtmlData: PropTypes.string,
}
export default MyComponent
