package algod

import (
	"context"

	"github.com/algorand/go-algorand-sdk/client/v2/common"
	"github.com/algorand/go-algorand-sdk/client/v2/common/models"
)

// SwaggerJSON /swagger.json
// Returns the entire swagger spec in json.
type SwaggerJSON struct {
	c *Client
}

// Do performs HTTP request
func (s *SwaggerJSON) Do(ctx context.Context,
	headers ...*common.Header) (response models.String, err error) {
	err = s.c.get(ctx, &response,
		"/swagger.json", nil, headers)
	return
}
