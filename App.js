import React from 'react';
import {
  View,
  StyleSheet,
} from 'react-native';
import { WebViewBridge, FullScreenWebViewBridge } from 'react-native-webview-bridge';

const styles = StyleSheet.create({
  webview: {
    flex: 1,
    backgroundColor: 'white',
  },
});

class App extends React.Component {
  constructor(props) {
    super(props);
    this.webview = null;
  }

  render() {
    /*
    <WebViewBridge
      ref={(webview) => { this.webview = webview; }}
      style={styles.webview}
      source={{ uri: 'http://m.youtube.com' }}
      javaScriptEnabled
      domStorageEnabled
    />
    */
    return (
      <View style={styles.webview}>
        <FullScreenWebViewBridge
          style={styles.webview}
        />
      </View>
    );
  }
}

module.exports = App;
