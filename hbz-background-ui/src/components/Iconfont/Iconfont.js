import React from 'react'
import PropTypes from 'prop-types'
import './iconfont.less'

const Iconfont = ({ type, colorful, style = {}, className = '' }) => {
  if (colorful) {
    return (<span
      dangerouslySetInnerHTML={{
        __html: `<svg class="colorful-icon" aria-hidden="true"><use xlink:href="#${type.startsWith('#') ? type.replace(/#/, '') : type}"></use></svg>`,
      }}
    />)
  }
  style['vertical-align'] = '-0.05em'
  return <i className={`iconfont icon-${type} ${className}`} style={style} />
}

Iconfont.propTypes = {
  type: PropTypes.string,
  colorful: PropTypes.bool,
  style: PropTypes.object,
  className: PropTypes.string,
}

export default Iconfont
